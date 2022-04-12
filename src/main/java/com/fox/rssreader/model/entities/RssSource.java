package com.fox.rssreader.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "rss_sources")
public class RssSource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 1024, nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean disabled = false;
}
