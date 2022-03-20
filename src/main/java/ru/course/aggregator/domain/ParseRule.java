package ru.course.aggregator.domain;

import lombok.Data;
import ru.course.aggregator.domain.enumeration.SourceType;

import javax.persistence.*;

@Data
@Entity
public class ParseRule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(length = 100, name = "source", nullable = false)
    @Enumerated(EnumType.STRING)
    private SourceType source;

    @Column(length = 1000, name = "feed_class")
    private String feedClass;
    @Column(length = 1000, name = "feed_tag")
    private String feedTag;

    @Column(length = 1000, name = "item_class")
    private String itemClass;
    @Column(length = 1000, name = "item_tag")
    private String itemTag;

    @Column(length = 1000, name = "title_class")
    private String titleClass;
    @Column(length = 1000, name = "title_tag")
    private String titleTag;

    @Column(length = 1000, name = "description_class")
    private String descriptionClass;
    @Column(length = 1000, name = "description_tag")
    private String descriptionTag;

    @Column(length = 1000, name = "publishedDate_class")
    private String publishedDateClass;
    @Column(length = 1000, name = "publishedDate_tag")
    private String publishedDateTag;

    @Column(length = 1000, name = "category_class")
    private String categoryClass;
    @Column(length = 1000, name = "category_tag")
    private String categoryTag;

    @Column(length = 1000, name = "img_class")
    private String imgClass;

    @Column(length = 1000, name = "author_class")
    private String authorClass;

    @Column(length = 1000, name = "uri_class")
    private String uriClass;
    @Column(length = 1000, name = "uri_tag")
    private String uriTag;

    @OneToOne(mappedBy="parseRule", cascade = CascadeType.ALL)
    private NewsSource newsSource;
}
