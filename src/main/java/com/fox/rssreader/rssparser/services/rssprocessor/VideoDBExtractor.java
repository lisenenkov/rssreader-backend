package com.fox.rssreader.rssparser.services.rssprocessor;

import com.fox.rssreader.rssparser.services.rssparser.RssItem;
import com.fox.rssreader.rssparser.videodblink.IMDBLink;
import com.fox.rssreader.rssparser.videodblink.KinopoiskLink;
import com.fox.rssreader.rssparser.videodblink.VideoDBLink;

import java.util.Vector;
import java.util.function.Function;
import java.util.regex.Pattern;

public class VideoDBExtractor {
  public Vector<VideoDBLink> getVideoLinks(RssItem rssitem) {
    var result = new Vector<VideoDBLink>();

    // imdb
    findVideoLinks("href=\\\"(?<url>[^\\s\"]*imdb\\.com[^\\s\"]+tt\\d{4,})", rssitem,
        (url) -> addLink(result, IMDBLink.createFromUrl(url)));

    // kinopoisk
    findVideoLinks("href=\\\"(?<url>[^\\s\"]*kinopoisk\\.ru[^\\s\"]+\\d{4,})", rssitem,
        (url) -> addLink(result, KinopoiskLink.createFromUrl(url)));

    // kinopoisk src
    findVideoLinks("src=\\\"(?<url>[^\\s\"]*kinopoisk\\.ru[^\\s\"]+\\d{4,})", rssitem,
        (url) -> addLink(result, KinopoiskLink.createFromUrl(url)));

    return result;
  }

  private void findVideoLinks(String patternString, RssItem rssitem, Function<String, Boolean> dbLinkCreator) {
    var pattern = Pattern.compile(patternString);
    var matcher = pattern.matcher(rssitem.getDescription());
    while (matcher.find()) {
      try {
        dbLinkCreator.apply(matcher.group("url"));
      } catch (Exception ignored) {
      }
    }
  }

  private boolean addLink(Vector<VideoDBLink> list, VideoDBLink link) {
    for (VideoDBLink videoDBLink : list) {
      if (videoDBLink.equals(link)) {
        return false;
      }
    }
    list.add(link);
    return true;
  }

}
