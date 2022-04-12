package com.fox.rssreader.model.services;

import com.fox.rssreader.model.entities.ExternalFilmInfo;
import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.FilmLink;
import com.fox.rssreader.model.entities.RssSource;
import com.fox.rssreader.model.ids.ExternalFilmInfoId;
import com.fox.rssreader.model.ids.FilmLinkId;
import com.fox.rssreader.rssparser.services.rssparser.RssItem;
import com.fox.rssreader.rssparser.services.rssprocessor.VideoDBExtractor;
import com.fox.rssreader.rssparser.videodblink.IMDBLink;
import com.fox.rssreader.rssparser.videodblink.KinopoiskLink;
import com.fox.rssreader.rssparser.videodblink.VideoDBLink;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmLinkSaveService {
    private final EntityManager entityManager;

    @Transactional
    public void SaveFilmLinkFromRssItem(RssSource rssSource, RssItem rssItem) {
        var videoDBExtractor = new VideoDBExtractor();
        var videoLinks = videoDBExtractor.getVideoLinks(rssItem);

        FilmLinkId filmLinkId = new FilmLinkId(rssSource, rssItem.getId());
        FilmLink filmLink = entityManager.find(FilmLink.class, filmLinkId);
        if (filmLink == null) {
            filmLink = new FilmLink();
            filmLink.setFilmLinkId(filmLinkId);
        }
        filmLink.setUrl(rssItem.getLink());
        filmLink.setCategory(rssItem.getTitleCategory());
        filmLink.setName(rssItem.getTitleName());
        filmLink.setYear(rssItem.getTitleYear());
        filmLink.setComment(rssItem.getTitleComment());
        filmLink.setPubDate(rssItem.getPubDate());
        filmLink.setDescription(rssItem.getDescription());

        List<ExternalFilmInfoId> newExternalFilmInfoIds = new ArrayList<>();

        Film film = null;
        for (var videoLink : videoLinks) {
            ExternalFilmInfoId externalFilmInfoId = new ExternalFilmInfoId(getSiteFromVideoLinkType(videoLink), videoLink.getId());
            var externalFilmInfo = entityManager.find(ExternalFilmInfo.class, externalFilmInfoId);
            if (externalFilmInfo != null) {
                if (film == null) {
                    film = externalFilmInfo.getFilm();
                }
            } else {
                newExternalFilmInfoIds.add(externalFilmInfoId);
            }
        }
        if (film == null) {
            film = new Film();
            film.setName(rssItem.getTitleName());
            film.setYear(rssItem.getTitleYear());
            entityManager.persist(film);
        }

        for (var externalFilmInfoId : newExternalFilmInfoIds) {
            ExternalFilmInfo externalFilmInfo = new ExternalFilmInfo();
            externalFilmInfo.setExternalFilmInfoId(externalFilmInfoId);
            externalFilmInfo.setFilm(film);
            entityManager.persist(externalFilmInfo);
        }

        filmLink.setFilm(film);
        entityManager.persist(filmLink);
    }

    private static ExternalFilmInfo.Site getSiteFromVideoLinkType(VideoDBLink dbLink) {
        if (dbLink instanceof IMDBLink) {
            return ExternalFilmInfo.Site.IMDB;
        }
        if (dbLink instanceof KinopoiskLink) {
            return ExternalFilmInfo.Site.Kinopoisk;
        }
        return null;
    }

}
