package com.fox.rssreader.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy ="film")
    private List<ExternalFilmInfo> externalFilmInfos = new ArrayList<>();

    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
    private List<FilmLink> links = new ArrayList<>();
}
