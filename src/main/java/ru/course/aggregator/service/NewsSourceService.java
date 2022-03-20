package ru.course.aggregator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.course.aggregator.domain.NewsSource;
import ru.course.aggregator.repository.NewsSourceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NewsSourceService {

    private final NewsSourceRepository newsSourceRepository;

    public NewsSourceService(NewsSourceRepository newsSourceRepository) {
        this.newsSourceRepository = newsSourceRepository;
    }

    @Transactional
    public void createSource(NewsSource newsSource){
        Optional<NewsSource> existedNewsSource = newsSourceRepository.findByUrl(newsSource.getUrl());
        if(existedNewsSource.isPresent()){
            newsSource = existedNewsSource.get();
            newsSource.setParseRule(newsSource.getParseRule());
        }
        newsSourceRepository.save(newsSource);
    }

    @Transactional
    public void updateSource(NewsSource newsSource){
        newsSourceRepository.save(newsSource);
    }

    @Transactional
    public void removeById(Long id) {
        newsSourceRepository.deleteById(id);
    }

    public List<NewsSource> findAll(){
        return newsSourceRepository.findAll();
    }
}
