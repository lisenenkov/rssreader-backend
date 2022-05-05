package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String token;
    private Boolean showViewed = false;
    private Boolean showIgnored = false;

    public UserDTO(User film) {
        this.name = film.getName();
        this.showViewed = film.getShowViewed();
        this.showIgnored = film.getShowIgnored();
    }
}
