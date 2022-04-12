package com.fox.rssreader.model.entities;

import com.fox.rssreader.model.ids.ExternalFilmInfoId;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "external_film_info", indexes = {@Index(columnList = "film_id")})
public class ExternalFilmInfo {
    @EmbeddedId
    private ExternalFilmInfoId externalFilmInfoId;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Film film;

    public enum Site {
        Kinopoisk, IMDB
    }
}
