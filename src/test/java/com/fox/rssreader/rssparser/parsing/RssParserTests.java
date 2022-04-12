package com.fox.rssreader.rssparser.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import com.fox.rssreader.rssparser.services.rssparser.RssItem;
import com.fox.rssreader.rssparser.services.rssparser.RssParser;
import com.fox.rssreader.rssparser.services.rssprocessor.VideoDBExtractor;
import com.fox.rssreader.rssparser.videodblink.IMDBLink;
import com.fox.rssreader.rssparser.videodblink.KinopoiskLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;

@SpringBootTest
public class RssParserTests {

    Vector<RssItem> rssItems;

    @BeforeEach
    void prepareRssItems() throws ParserConfigurationException, SAXException, IOException {
        rssItems = new Vector<>();
        var rssParser = new RssParser("./src/test/static/rsssource1.xml", rssItem -> rssItems.add(rssItem));
        rssParser.parse();
    }

    @Test
    @DisplayName("parse sample file")
    void parseFile() {

        assertEquals(1, rssItems.size());
        assertEquals("12345678", rssItems.get(0).getId());
        assertEquals("https://test/sets?t=12345678", rssItems.get(0).getLink());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(2021, Calendar.JUNE, 24, 16, 32, 34);
        assertEquals(calendar.getTime().toString(), rssItems.get(0).getPubDate().toString());
        assertTrue(rssItems.get(0).getDescription().length() > 100);
        assertEquals("Category1", rssItems.get(0).getCategory());

        assertEquals("Category", rssItems.get(0).getTitleCategory());
        assertEquals("Title", rssItems.get(0).getTitleName());
        assertEquals("2020", rssItems.get(0).getTitleYear());
        assertEquals("1999p", rssItems.get(0).getTitleComment());

        assertEquals("descriptiondescriptiondescriptiondescription", rssItems.get(0).getDescription().substring(0, 44));
    }

    @Test
    @DisplayName("get video db links from item description")
    void extractVideoLinks() {
        var extractor = new VideoDBExtractor();
        var links = extractor.getVideoLinks(rssItems.get(0));

        assertEquals(2, links.size());

        var imdbLink = new IMDBLink();
        imdbLink.setId("tt12345");
        assertEquals(imdbLink, links.get(0));

        var kinopoiskLink = new KinopoiskLink();
        kinopoiskLink.setId("54321");
        assertEquals(kinopoiskLink, links.get(1));

    }

}
