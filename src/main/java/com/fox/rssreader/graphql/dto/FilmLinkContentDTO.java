package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.FilmLink;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmLinkContentDTO {
    private Boolean viewed;

    public void modifyFilmLink(FilmLink filmLink) {
        if (getViewed() != null) filmLink.setViewed(getViewed());
    }
}
