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
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureGraphQlTester
class FilmControllerTest {
    Film film;
    @Autowired
    private GraphQlTester graphQlTester;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void beforeTest() {
        var rssSource = new RssSource();
        rssSource.setName("name");
        rssSource.setUrl("http://name");
        entityManager.persist(rssSource);

        this.film = new Film();
        film.setName("film name");
        film.setYear("2999");
        entityManager.persist(film);

        var externalFilmInfo = new ExternalFilmInfo();
        externalFilmInfo.setExternalFilmInfoId(new ExternalFilmInfoId(ExternalFilmInfo.Site.IMDB, "112"));
        externalFilmInfo.setFilm(film);
        entityManager.persist(externalFilmInfo);

        var filmLink1 = new FilmLink();
        filmLink1.setFilmLinkId(new FilmLinkId(rssSource, "abc123"));
        filmLink1.setFilm(film);
        filmLink1.setUrl("http://name/1");
        filmLink1.setCategory("film");
        filmLink1.setName(film.getName());
        filmLink1.setPubDate(new Date());
        filmLink1.setDescription("desc");
        entityManager.persist(filmLink1);

        var filmLink2 = new FilmLink();
        filmLink2.setFilmLinkId(new FilmLinkId(rssSource, "abc124"));
        filmLink2.setFilm(film);
        filmLink2.setUrl("http://name/2");
        filmLink2.setCategory("film");
        filmLink2.setName(film.getName());
        filmLink2.setPubDate(new Date());
        filmLink2.setDescription("desc");
        entityManager.persist(filmLink2);

        entityManager.flush();
        entityManager.refresh(film);
    }


    @Test
    @Transactional
    void film() {
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
                    assertThat(film.getExternalInfo(), is(hasSize(1)));
                    assertThat(film.getLinks(), is(hasSize(2)));
                });
    }

    @Test
    @Transactional
    void films() {
        graphQlTester.document(
                        """
                                query films {
                                  films {
                                  id, name, year, ignore, externalInfo { site, externalId}}
                                }""")
                .execute()
                .path("films")
                .entityList(FilmDTO.class)
                .satisfies(films -> {
                    assertThat(films.stream().filter(f -> "film name".equals(f.getName())).count(), is(1L));
                    var optionalFilm = films.stream().filter(f -> "film name".equals(f.getName())).findFirst();
                    assertTrue(optionalFilm.isPresent());
                    var film = optionalFilm.get();
                    assertThat(film.getYear(), is("2999"));
                    assertThat(film.getIgnore(), is(false));
                });
    }

    @Test
    @Transactional
    void updateFilm() {
        graphQlTester.document(
                        """
                                mutation film($id: ID!) {
                                  updateFilm(id: $id, film: { ignore: true }) {
                                  id, ignore}
                                }""")
                .variable("id", film.getId().toString())
                .execute()
                .path("updateFilm")
                .entity(FilmDTO.class)
                .satisfies(film -> assertThat(film.getIgnore(), is(true)));
    }

    @Test
    @Transactional
    void updateFilmLink() {
        var optionalFilmLink = film.getLinks().stream().findFirst();
        assertTrue(optionalFilmLink.isPresent());
        var filmLink = optionalFilmLink.get();

        graphQlTester.document(
                        """
                                mutation filmLink($id: ID!, $rssSource: ID!) {
                                  updateFilmLinks(ids: [{ id: $id, rssSource: $rssSource }],
                                    filmLink: {viewed: true}) {
                                  id, rssSource,  viewed}
                                }""")
                .variable("id", filmLink.getFilmLinkId().getId())
                .variable("rssSource", filmLink.getFilmLinkId().getRssSource().getId())
                .execute()
                .path("updateFilmLinks")
                .entityList(FilmLinkDTO.class)
                .satisfies(filmLinksDTO -> {
                    for (var filmLinkDTO : filmLinksDTO) {
                        assertThat(filmLinkDTO.getViewed(), is(true));
                    }
                });
    }
}