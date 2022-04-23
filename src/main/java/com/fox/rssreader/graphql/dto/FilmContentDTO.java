package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.RssSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmContentDTO {
    private Boolean ignore;

    public void modifyFilm(Film film) {
        if (getIgnore() != null) film.setIgnore(getIgnore());
    }
}
