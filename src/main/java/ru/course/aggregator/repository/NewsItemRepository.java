package ru.course.aggregator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.aggregator.domain.NewsItem;

public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {

    NewsItem findByLink(String link);

    Page<NewsItem> findByTitleContainsAllIgnoreCase(String title, Pageable pageable);

    long countByTitleContainsAllIgnoreCase(String title);
}
