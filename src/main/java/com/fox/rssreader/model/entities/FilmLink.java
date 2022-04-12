package com.fox.rssreader.model.entities;

import com.fox.rssreader.model.ids.FilmLinkId;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "film_links")
public class FilmLink {
    @EmbeddedId
    private FilmLinkId filmLinkId;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Film film;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 1000)
    private String category;

    @Column(length = 1000)
    private String name;

    @Column(length = 20, name = "film_year")
    private String year;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private Date pubDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean viewed = false;

    public FilmLink(FilmLinkId filmLinkId) {
        this.filmLinkId = filmLinkId;
    }
}
