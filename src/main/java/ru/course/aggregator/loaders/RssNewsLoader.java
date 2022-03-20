package ru.course.aggregator.loaders;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import ru.course.aggregator.domain.NewsItem;
import ru.course.aggregator.domain.ParseRule;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RssNewsLoader implements INewsLoader{

    private final Logger logger = LoggerFactory.getLogger(RssNewsLoader.class);

    @Override
    public List<NewsItem> loadNewsFeedFromUrlByRules(final String url, final ParseRule rule) throws IOException {
        List<NewsItem> newsItems = new ArrayList<>();

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
        try {
            feed = input.build(new XmlReader(new URL(url)));
        } catch (FeedException e) {
            logger.error("Bad rss income", e);
            return newsItems;
        }

        if(feed.getEntries() != null){
            for (Object o : feed.getEntries()){
                NewsItem newsItem = createNewsItemFromEntry((SyndEntry) o);
                newsItems.add(newsItem);
            }
        }

        return newsItems;
    }

    private NewsItem createNewsItemFromEntry(SyndEntry entry){
        NewsItem newsItem = new NewsItem();
        newsItem.setTitle(entry.getTitle());
        newsItem.setLink(entry.getLink());
        newsItem.setDescription(entry.getDescription().getValue());
        newsItem.setPublishedDate(entry.getPublishedDate().toString());
        newsItem.setAuthor(entry.getAuthor());
        List categories = entry.getCategories();
        if(!CollectionUtils.isEmpty(categories)){
            SyndCategory syndCategory = (SyndCategory) categories.get(0);
            newsItem.setCategory(syndCategory.getName());
        }
        List enclosures = entry.getEnclosures();
        if(!CollectionUtils.isEmpty(enclosures)){
            SyndEnclosure syndEnclosure = (SyndEnclosure) enclosures.get(0);
            newsItem.setImgSrc(syndEnclosure.getUrl());
        }

        return newsItem;
    }
}
