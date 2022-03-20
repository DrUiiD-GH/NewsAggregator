package ru.course.aggregator.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewsSource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(min = 1, message = "Invalid URL")
    @URL(message = "Invalid URL")
    @Column(length = 1000)
    private String url;

    @Size(min = 1, message = "Name must be at least 1 character")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parse_rule_id")
    private ParseRule parseRule;

    @OneToMany(mappedBy = "newsSource", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<NewsItem> newsItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NewsSource that = (NewsSource) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
