package com.fox.rssreader.rssparser.videodblink;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

public class IMDBLink extends VideoDBLink {
  public static @NotNull
  IMDBLink createFromUrl(String url){
    var imdbLink = new IMDBLink();
    imdbLink.setUrl(url);
    return imdbLink;
  }
  
  @Override
  public String getUrl() {
    return String.format("https://www.imdb.com/title/%s/", getId());
  }

  @Override
  public void setUrl(String url) {
    var pattern = Pattern.compile(".*imdb.*?(?<id>tt\\d{4,})");
    var matcher = pattern.matcher(url);
    if (!matcher.find()) {
      throw new InvalidParameterException("Invalid IMDB url");
    }
    setId(matcher.group("id"));
  }
}
