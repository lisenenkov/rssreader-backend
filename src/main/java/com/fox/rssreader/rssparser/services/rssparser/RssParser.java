package com.fox.rssreader.rssparser.services.rssparser;

import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RssParser {
  private String source;
  private IRssItemProcessor rssItemProcessor;

  public void parse() throws ParserConfigurationException, SAXException, IOException {
    var saxParserFactory = SAXParserFactory.newInstance();
    var saxParser = saxParserFactory.newSAXParser();

    SAX_RSS_Handler handler = new SAX_RSS_Handler(rssItemProcessor);
    saxParser.parse(source, handler);
  }

  private class SAX_RSS_Handler extends DefaultHandler {
    private Stack<String> tags;
    private RssItem rssItem;
    private StringBuilder elementValue;
    private final IRssItemProcessor rssItemProcessor;

    public SAX_RSS_Handler(IRssItemProcessor rssItemProcessor) {
      this.rssItemProcessor = rssItemProcessor;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
      elementValue.append(ch, start, length);
    }

    @Override
    public void startDocument() {
      tags = new Stack<>();
      rssItem = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
      tags.push(qName);
      if ("item".equals(qName)) {
        if ("rss/channel/item".equals(String.join("/", tags))) {
          rssItem = new RssItem();
        }
      }
      elementValue = new StringBuilder();

    }

    @Override
    public void endElement(String uri, String localName, String qName) {
      tags.pop();
      if (tags.size() < 3 && rssItem != null) {
        rssItemProcessor.processItem(rssItem);
        rssItem = null;
      } else if (tags.size() == 3 && rssItem != null) {
        if ("link".equals(qName)) {
          rssItem.setLink(elementValue.toString());
        } else if ("title".equals(qName)) {
          rssItem.setTitle(elementValue.toString());
        } else if ("pubDate".equals(qName)) {
          rssItem.setPubDate(elementValue.toString());
        } else if ("description".equals(qName)) {
          rssItem.setDescription(elementValue.toString());
        } else if ("category".equals(qName)) {
          if (rssItem.getCategory() == null || rssItem.getCategory().isEmpty()) {
            rssItem.setCategory(elementValue.toString());
          }
        }
      }
    }

  }

}
