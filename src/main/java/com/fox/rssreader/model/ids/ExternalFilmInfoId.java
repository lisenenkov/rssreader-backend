package com.fox.rssreader.model.ids;

import com.fox.rssreader.model.entities.ExternalFilmInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalFilmInfoId implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ExternalFilmInfo.Site site;

    @Column(length = 20)
    private String externalId;
}
