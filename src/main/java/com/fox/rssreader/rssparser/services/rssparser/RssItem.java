package com.fox.rssreader.rssparser.services.rssparser;

import lombok.Getter;
import lombok.Setter;

import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

@Getter
public class RssItem {
  private String id;
  private String link;

  private String titleCategory;
  private String titleName;
  private String titleYear;
  private String titleComment;

  private Date pubDate;
  @Setter
  private String description;
  @Setter
  private String category;

  public void setLink(String link) {
    this.link = link;
    var pattern = Pattern.compile("=(?<id>\\d+)$");
    var matcher = pattern.matcher(link);
    if (!matcher.find() && matcher.group("id") == null) {
      throw new InvalidParameterException("Invalid link");
    }
    id = matcher.group("id");
  }

  public void setTitle(String title) {
    var pattern = Pattern.compile("^(?<category>.+?) +:: +(?<name>.+?)( +\\((?<year>\\d{4})\\) *(?<comment>.*))?$");
    var matcher = pattern.matcher(title);
    if (matcher.find()) {
      this.titleCategory = matcher.group("category");
      this.titleName = matcher.group("name");
      this.titleYear = matcher.group("year");
      this.titleComment = matcher.group("comment");
    }
  }

  public void setPubDate(String pubDate) {
    var dateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
    try {
      this.pubDate = Date.from(ZonedDateTime.from(dateFormat.parse(pubDate)).toInstant());
    } catch (Exception e) {
      this.pubDate = new Date();
    }
  }

}
