package com.fox.rssreader.controllers;

import com.fox.rssreader.graphql.dto.*;
import com.fox.rssreader.model.ids.FilmLinkId;
import com.fox.rssreader.model.repositories.FilmLinkRepository;
import com.fox.rssreader.model.repositories.FilmRepository;
import com.fox.rssreader.model.repositories.RssSourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@AllArgsConstructor
public class FilmController {
    private final FilmRepository filmRepository;
    private final FilmLinkRepository filmLinkRepository;
    private final RssSourceRepository rssSourceRepository;

    @QueryMapping
    @Transactional
    public List<FilmDTO> films(@Argument Boolean ignore) {
        return filmRepository.findLastPublished(ignore)
                .stream()
                .map(film -> {
                    var filmDTO = new FilmDTO(film);
                    filmDTO.fillExternalInfo(film);
                    return filmDTO;
                })
                .toList();
    }

    @QueryMapping
    @Transactional
    public FilmDTO film(@Argument Long id) {

        var film = filmRepository.findById(id);
        if (film.isEmpty()) {
            return null;
        }

        var filmDTO = new FilmDTO(film.get());
        filmDTO.fillExternalInfo(film.get());
        filmDTO.fillLinks(film.get());
        return filmDTO;

    }

    @MutationMapping
    @Transactional
    public FilmDTO updateFilm(@Argument Long id, @Argument(name = "film") FilmContentDTO filmContent) {
        if (filmContent == null) return null;
        var optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isEmpty()) return null;
        var film = optionalFilm.get();
        filmContent.modifyFilm(film);
        filmRepository.save(film);

        return new FilmDTO(film);
    }

    @MutationMapping
    @Transactional
    public List<FilmLinkDTO> updateFilmLinks(@Argument(name = "ids")  List<FilmLinkIdDTO> ids,
                                            @Argument(name = "filmLink") FilmLinkContentDTO filmLinkContent) {
        if (filmLinkContent == null) return null;

        var filmLinkIds = ids.stream()
                .map(id_value -> new FilmLinkId(
                        rssSourceRepository.getReferenceById(id_value.getRssSource()),
                        id_value.getId()))
                .toList();
        var filmLinks = filmLinkRepository.findAllById(filmLinkIds);
        for (var filmLink : filmLinks) {
            filmLinkContent.modifyFilmLink(filmLink);
        }
        filmLinkRepository.saveAll(filmLinks);

        return filmLinks.stream()
                .map(FilmLinkDTO::new)
                .toList();
    }

}
