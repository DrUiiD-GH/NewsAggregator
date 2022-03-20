package ru.course.aggregator.utils;

import ru.course.aggregator.domain.enumeration.SourceType;
import ru.course.aggregator.loaders.HtmlNewsLoader;
import ru.course.aggregator.loaders.INewsLoader;
import ru.course.aggregator.loaders.RssNewsLoader;

public class NewsUtils {

    private NewsUtils(){}

    public static INewsLoader selectNewsLoaderBySourceType(final SourceType sourceType){
        switch (sourceType){
            case RSS:
                return new RssNewsLoader();
            case HTML:
                return new HtmlNewsLoader();
            default:
                return new HtmlNewsLoader();
        }
    }
}
