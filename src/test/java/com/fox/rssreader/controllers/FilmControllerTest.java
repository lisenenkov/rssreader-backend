package com.fox.rssreader.controllers;

import com.fox.rssreader.graphql.dto.FilmDTO;
import com.fox.rssreader.graphql.dto.FilmLinkDTO;
import com.fox.rssreader.model.entities.ExternalFilmInfo;
import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.FilmLink;
import com.fox.rssreader.model.entities.RssSource;
import com.fox.rssreader.model.ids.ExternalFilmInfoId;
import com.fox.rssreader.model.ids.FilmLinkId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureGraphQlTester
class FilmControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;
    @Autowired
    private EntityManager entityManager;

    Film film;

    @BeforeEach
    void beforeTest() {
        var rssSource = new RssSource();
        rssSource.setName("name");
        rssSource.setUrl("http://name");
        entityManager.persist(rssSource);

        this.film = new Film();
        film.setName("film name");
        film.setYear("2999");

        var externalFilmInfo = new ExternalFilmInfo();
        externalFilmInfo.setExternalFilmInfoId(new ExternalFilmInfoId(ExternalFilmInfo.Site.IMDB, "112"));
        List<ExternalFilmInfo> externalFilmInfos = new ArrayList<>();
        externalFilmInfos.add(externalFilmInfo);
        film.setExternalFilmInfos(externalFilmInfos);

        var filmLink = new FilmLink();
        filmLink.setFilmLinkId(new FilmLinkId(rssSource, "abc123"));
        List<FilmLink> filmLinks = new ArrayList<>();
        filmLinks.add(filmLink);
        film.setLinks(filmLinks);

        entityManager.persist(film);
    }


    @Test
    @Transactional
    void film() {
        //select
        graphQlTester.document(
                        """
                                query film($id: ID!) {
                                  film(id: $id) {
                                  id, name, year,  externalInfo { site, externalId}, links { name, url, category }}
                                }""")
                .variable("id", film.getId().toString())
                .execute()
                .path("film")
                .entity(FilmDTO.class)
                .satisfies(film -> {
                    assertThat(film.getYear(), is("2999"));
                    //assertThat(film.getLinks(), is(hasSize(1)));
                });
    }

    @Test
    @Transactional
    void films() {
        //select
        graphQlTester.document(
                        """
                                query films {
                                  films {
                                  id, name, year,  externalInfo { site, externalId}}
                                }""")
                .execute()
                .path("films")
                .entityList(FilmDTO.class)
                .satisfies(films -> {
                    var film = films.stream().filter(f -> f.getName() == "film name").findFirst().get();
                    assertThat(film.getYear(), is("2999"));
                });
    }
}