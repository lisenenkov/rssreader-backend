package com.fox.rssreader.controllers;

import com.fox.rssreader.graphql.dto.FilmDTO;
import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.repositories.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@AllArgsConstructor
public class FilmController {
    private final FilmRepository filmRepository;

    @QueryMapping
    @Transactional
    public List<FilmDTO> films(@Argument Boolean ignore) {

        List<Film> films;
        if (ignore == null) {
            films = filmRepository.findAll();
        }else{
            films = filmRepository.findAllByIgnore(ignore);
        }
        return films
                .stream()
                .map(film -> {
                    var filmDTO = new FilmDTO(film);
                    filmDTO.fillExternalInfo(film);
                    return  filmDTO;
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
        return  filmDTO;

    }

}
