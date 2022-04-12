package com.fox.rssreader.rssparser.storing;

import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.RssSource;
import com.fox.rssreader.model.services.FilmLinkSaveService;
import com.fox.rssreader.rssparser.services.rssparser.RssItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Transactional
public class FilmLinkSaveServiceTest {

    private FilmLinkSaveService filmLinkSaveService;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void beforeTest() {
        filmLinkSaveService = new FilmLinkSaveService(entityManager);
    }

    @Test
    @Transactional
    @DisplayName("save RSS item for new film")
    void saveRSSItem() {
        var film = new Film();
        film.setName("film name");
        film.setYear("2999");
        entityManager.persist(film);

        var rssSource = new RssSource();
        rssSource.setName("test");
        rssSource.setUrl("http:/0.0.0.0");
        rssSource.setDisabled(false);
        entityManager.persist(rssSource);

        var rssItem = new RssItem();
        rssItem.setLink("https://test/sets?t=12345678");
        rssItem.setTitle("Category :: Title (2020) 1999p");
        rssItem.setPubDate("Thu, 24 Jun 2021 16:32:34 GMT");
        rssItem.setDescription("descriptiondescriptiondescriptiondescription " + "a href=\"https://www.imdb.com/title/tt12345/?ref_=plg_rt_1\" " + ";a href=\"https://www.kinopoisk.ru/film/54321/\"");
        rssItem.setCategory("category");

        //first link
        filmLinkSaveService.SaveFilmLinkFromRssItem(rssSource, rssItem);

        //second link
        rssItem.setLink("https://test/sets?t=12345679");
        filmLinkSaveService.SaveFilmLinkFromRssItem(rssSource, rssItem);

        var newFilm = (Film) entityManager
                .createQuery("SELECT film FROM Film film WHERE film.name LIKE :name")
                .setParameter("name", "Title").getSingleResult();

        entityManager.refresh(newFilm);

        assertThat(newFilm, is(notNullValue()));
        assertThat(newFilm.getExternalFilmInfos(), hasSize(2));
        assertThat(newFilm.getLinks(), hasSize(2));
    }

}
