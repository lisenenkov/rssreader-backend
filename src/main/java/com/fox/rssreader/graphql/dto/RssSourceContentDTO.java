package com.fox.rssreader.graphql.dto;

import com.fox.rssreader.model.entities.RssSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RssSourceContentDTO {
    private String name;
    private String url;
    private Boolean disabled;

    public RssSourceContentDTO(RssSource rssSource) {
        this.name = rssSource.getName();
        this.url = rssSource.getUrl();
        this.disabled = rssSource.getDisabled();
    }

    public void modifyRssSource(RssSource rssSource) {
        if (getName() != null) rssSource.setName(getName());
        if (getUrl() != null) rssSource.setUrl(getUrl());
        if (getDisabled() != null) rssSource.setDisabled(getDisabled());
    }

    public RssSource toRssSource() {
        RssSource rssSource = new RssSource();
        modifyRssSource(rssSource);
        return rssSource;
    }

}
