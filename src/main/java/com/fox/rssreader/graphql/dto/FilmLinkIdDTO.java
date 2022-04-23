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
public class FilmLinkIdDTO {
    private String id;
    private Long rssSource;
}
