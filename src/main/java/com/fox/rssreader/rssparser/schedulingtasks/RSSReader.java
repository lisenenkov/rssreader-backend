package com.fox.rssreader.rssparser.schedulingtasks;

import com.fox.rssreader.model.repositories.RssSourceRepository;
import com.fox.rssreader.model.services.FilmLinkSaveService;
import com.fox.rssreader.rssparser.services.rssparser.RssParser;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class RSSReader {
    private RssSourceRepository rssSourceRepository;
    private FilmLinkSaveService filmLinkSaveService;

    //@Scheduled(fixedRate = 3600_000)
    public void process() {
        for (var rssSource: rssSourceRepository.findByDisabledIsFalse()) {
            try {
                var rssParser = new RssParser(rssSource.getUrl(), rssItem -> filmLinkSaveService.SaveFilmLinkFromRssItem(rssSource, rssItem));
                rssParser.parse();

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
