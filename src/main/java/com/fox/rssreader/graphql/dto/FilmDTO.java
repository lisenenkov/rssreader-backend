package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO {
    List<ExternalFilmInfoDTO> externalInfo;
    List<FilmLinkDTO> links;
    private Long id;
    private String name;
    private String year;
    private Boolean ignore;

    public FilmDTO(Film film) {
        this.id = film.getId();
        this.name = film.getName();
        this.year = film.getYear();
        this.ignore = film.getIgnore();
    }

    public void fillExternalInfo(Film film) {
        this.externalInfo = film.getExternalFilmInfos()
                .stream()
                .map(ExternalFilmInfoDTO::new)
                .toList();
    }

    public void fillLinks(Film film) {
        this.links = film.getLinks()
                .stream()
                .map(FilmLinkDTO::new)
                .toList();
    }
}
