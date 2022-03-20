package ru.course.aggregator.loaders;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import ru.course.aggregator.domain.NewsItem;
import ru.course.aggregator.domain.ParseRule;
import ru.course.aggregator.exceptions.BadRuleException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class HtmlNewsLoader implements INewsLoader{

    private final String IMG_TAG_NAME = "img";
    private final String IMG_LINK_ATTR = "src";
    private final String LINK_HREF_ATTR = "href";

    @Override
    public List<NewsItem> loadNewsFeedFromUrlByRules(final String url, final ParseRule rule) throws IOException, BadRuleException {
        checkParseRule(rule);

        final ArrayList<NewsItem> newsItems = new ArrayList<>();

        Connection.Response connection;
        connection = Jsoup.connect(url).execute();
        Document document = connection.parse();
        if (!StringUtils.isEmpty(rule.getFeedClass())) {
            document.body().getElementsByClass(rule.getFeedClass()).forEach(getFeedConsumer(rule, newsItems));
        } else if(!StringUtils.isEmpty(rule.getFeedTag())){
            document.body().getElementsByTag(rule.getFeedTag()).forEach(getFeedConsumer(rule, newsItems));
        } else {
            if(!StringUtils.isEmpty(rule.getItemClass())) {
                document.body().getElementsByClass(rule.getItemClass()).forEach(getElementConsumer(rule, newsItems));
            } else if(!StringUtils.isEmpty(rule.getItemTag())){
                document.body().getElementsByTag(rule.getItemTag()).forEach(getElementConsumer(rule, newsItems));
            }
        }
        return newsItems;
    }

    private void checkParseRule(final ParseRule rule) throws BadRuleException {
        String formatForMessage = "Required field '%s' doesn't exist";
        if(StringUtils.isEmpty(rule.getItemClass())){
            throw new BadRuleException(String.format(formatForMessage, "itemClass"));
        }
        if(StringUtils.isEmpty(rule.getTitleClass())){
            throw new BadRuleException(String.format(formatForMessage, "titleClass"));
        }

    }

    private Consumer<Element> getFeedConsumer(ParseRule rule, ArrayList<NewsItem> newsItems) {
        return value -> {
            if (!StringUtils.isEmpty(rule.getItemClass())) {
                value.getElementsByClass(rule.getItemClass()).forEach(getElementConsumer(rule, newsItems));
            } else if (!StringUtils.isEmpty(rule.getItemTag())) {
                value.getElementsByTag(rule.getItemTag()).forEach(getElementConsumer(rule, newsItems));
            }
        };
    }

    private Consumer<Element> getElementConsumer(ParseRule rule, ArrayList<NewsItem> newsItems) {
        return e -> {
            String title = getElementTextByClassAndTag(e, rule.getTitleClass(), rule.getTitleTag(), null);
            String description = getElementTextByClassAndTag(e, rule.getDescriptionClass(), rule.getDescriptionTag(), null);
            if (StringUtils.isEmpty(title))
                return;
            NewsItem newsItem = new NewsItem();
            newsItem.setTitle(title);
            newsItem.setDescription(description);
            newsItem.setCategory(getElementTextByClassAndTag(e, rule.getCategoryClass(), rule.getCategoryTag(), "Без категории"));
            newsItem.setAuthor(getElementTextByClassAndTag(e, rule.getAuthorClass(), null, "Неизвестен"));
            newsItem.setPublishedDate(getElementTextByClassAndTag(e, rule.getPublishedDateClass(), rule.getPublishedDateTag(), "Неизвестно"));
            newsItem.setImgSrc(getElementAttrByClassAndTag(e, rule.getImgClass(), IMG_TAG_NAME, IMG_LINK_ATTR));

            String linkAttr = getElementAttrByClassAndTag(e, rule.getUriClass(), rule.getUriTag(), LINK_HREF_ATTR);
            if(StringUtils.isEmpty(linkAttr)){
                linkAttr = getElementTextByClassAndTag(e, rule.getUriClass(), rule.getUriTag(), null);
            }
            newsItem.setLink(linkAttr);

            newsItems.add(newsItem);
        };
    }

    private String getElementTextByClassAndTag(final Element e, final String className, final String tagName, final String nonExistedText){
        Elements elements = getElementsByClassAndTag(e, className, tagName);

        if(elements == null || StringUtils.isEmpty(elements.text())){
            return nonExistedText;
        }
        return elements.text();
    }

    private String getElementAttrByClassAndTag(final Element e, final String className, final String tagName, final String attrName){
        Elements elements = getElementsByClassAndTag(e, className, tagName);

        if(elements == null){
            return null;
        }
        return elements.attr(attrName);
    }

    private Elements getElementsByClassAndTag(final Element e, final String className, final String tagName){
        Elements elements = null;
        if (!StringUtils.isEmpty(className)) {
            elements = e.getElementsByClass(className);
            if (!StringUtils.isEmpty(tagName)) {
                elements = elements.tagName(tagName);
            }
        }

        return elements;
    }
}
