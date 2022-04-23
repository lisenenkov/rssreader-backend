package com.fox.rssreader.model.entities;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 500, nullable = false)
    private String name;

    @Column(length = 20, name = "film_year")
    private String year;

    @Column(nullable = false)
    private Boolean ignore = false;

    @OneToMany(mappedBy ="film", fetch = FetchType.LAZY)
    @OrderBy("externalFilmInfoId.site")
    private Set<ExternalFilmInfo> externalFilmInfos = new HashSet<>();

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    @OrderBy("pubDate DESC")
    private Set<FilmLink> links = new HashSet<>();
}
