package ru.course.aggregator.service.mapper;

import ru.course.aggregator.domain.NewsSource;
import ru.course.aggregator.web.dto.NewsSourceDTO;

public class NewsSourceMapper {

    private NewsSourceMapper(){}

    public static NewsSourceDTO toDTO(NewsSource newsSource){
        NewsSourceDTO newsSourceDTO = new NewsSourceDTO();
        newsSourceDTO.setId(newsSource.getId());
        newsSourceDTO.setName(newsSource.getName());
        newsSourceDTO.setUrl(newsSource.getUrl());
        newsSourceDTO.setRuleId(newsSource.getParseRule().getId());

        return newsSourceDTO;
    }
}
