package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.FilmLink;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmLinkDTO {
    private String id;
    private Long rssSource;
    private String url;
    private String category;
    private String name;
    private String year;
    private String comment;
    private Date pubDate;
    private String description;

    public FilmLinkDTO(FilmLink filmLink){
        this.id = filmLink.getFilmLinkId().getId();
        this.rssSource = filmLink.getFilmLinkId().getRssSource().getId();
        this.url = filmLink.getUrl();
        this.category = filmLink.getCategory();
        this.name = filmLink.getName();
        this.year = filmLink.getYear();
        this.comment = filmLink.getComment();
        this.pubDate = filmLink.getPubDate();
        this.description = filmLink.getDescription();
    }

}
