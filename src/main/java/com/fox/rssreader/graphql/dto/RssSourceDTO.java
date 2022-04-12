package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.RssSource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RssSourceDTO extends RssSourceContentDTO{
    private Long id;

    public RssSourceDTO(RssSource rssSource) {
        super(rssSource);
        this.id = rssSource.getId();
    }
}
