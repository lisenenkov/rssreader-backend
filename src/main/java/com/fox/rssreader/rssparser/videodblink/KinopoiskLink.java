package com.fox.rssreader.rssparser.videodblink;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

public class KinopoiskLink extends VideoDBLink {
    public static KinopoiskLink createFromUrl(String url) {
        var kinopoiskLink = new KinopoiskLink();
        kinopoiskLink.setUrl(url);
        return kinopoiskLink;
    }

    @Override
    public String getUrl() {
        return String.format("https://www.kinopoisk.ru/film/%s/", getId());
    }

    @Override
    public void setUrl(String url) {
        var pattern = Pattern.compile(".*kinopoisk\\.ru.*?(?<id>\\d{4,})");
        var matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new InvalidParameterException("Invalid Kinopoisk url");
        }
        setId(matcher.group("id"));
    }
}
