package ru.course.aggregator.web.dto;

import lombok.Data;

@Data
public class NewsSourceDTO {
    private Long id;
    private String url;
    private String name;
    private Long ruleId;
}
