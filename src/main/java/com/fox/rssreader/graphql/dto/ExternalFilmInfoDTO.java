package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.ExternalFilmInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalFilmInfoDTO {
    private ExternalFilmInfo.Site site;
    private String externalId;

    public ExternalFilmInfoDTO(ExternalFilmInfo externalFilmInfo){
        this.site = externalFilmInfo.getExternalFilmInfoId().getSite();
        this.externalId = externalFilmInfo.getExternalFilmInfoId().getExternalId();
    }
}
