package com.fox.rssreader.model.ids;

import com.fox.rssreader.model.entities.RssSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmLinkId implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RssSource rssSource;

    @Column(nullable = false, length = 100)
    private String id;
}