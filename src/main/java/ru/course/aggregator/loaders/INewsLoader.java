package ru.course.aggregator.loaders;

import ru.course.aggregator.domain.NewsItem;
import ru.course.aggregator.domain.ParseRule;
import ru.course.aggregator.exceptions.BadRuleException;

import java.io.IOException;
import java.util.List;

public interface INewsLoader {
    List<NewsItem> loadNewsFeedFromUrlByRules(final String url, final ParseRule rule) throws IOException, BadRuleException;
}
