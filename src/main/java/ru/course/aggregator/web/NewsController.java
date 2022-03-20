package ru.course.aggregator.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.course.aggregator.web.dto.NewsSourceDTO;
import ru.course.aggregator.web.dto.SearchDTO;
import ru.course.aggregator.exceptions.BadRuleException;
import ru.course.aggregator.service.NewsService;

import javax.persistence.NonUniqueResultException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class NewsController {

    private final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }


    @GetMapping("/")
    public String main(Model model) {
        List<NewsSourceDTO> newsSources = newsService.getExistedNewsSources();
        model.addAttribute("newsSource", new NewsSourceDTO());
        model.addAttribute("addedNewsSources", newsSources);

        return "addNewsSource";
    }

    @PostMapping("/addNewsSource")
    public String addNewsSourceSubmit(@RequestParam("file") MultipartFile file,
                                      @ModelAttribute NewsSourceDTO newsSource) {
        try {
            newsService.loadNewsSource(newsSource, file.getInputStream());
        } catch (NonUniqueResultException e){
            logger.error("This source url been already added", e);
        } catch (IOException e){
            logger.error("There is a problem with loading news feed (maybe internet is down)", e);
        } catch (BadRuleException e) {
            logger.error("There is error in rule", e);
        }

        return "redirect:/";
    }

    @GetMapping("/removeNewsSource")
    public String removeNewsSource(@Param(value="ruleId") Long id) {
        newsService.removeNewsSource(id);

        return "redirect:/";
    }

    @GetMapping("/downloadRule")
    @ResponseBody
    public ResponseEntity downloadRule(@Param(value="ruleId") Long ruleId) {
        byte[] fileContent = newsService.downloadRule(ruleId).getBytes();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileContent));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=rule.yml")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(fileContent.length)
                .body(resource);
    }

    @GetMapping("/newsList")
    public String newsList(Model model,
                           @RequestParam(defaultValue="1", value="page", required=false) Integer page,
                           @RequestParam(defaultValue="20", value="size", required=false) Integer size,
                           @RequestParam(defaultValue="", value="search", required=false) String search) {
        model.addAttribute("search", new SearchDTO(search));
        model.addAttribute("newsItems", newsService.getNewsItems(page - 1, size, search));
        model.addAttribute("currentPage", page);
        float nrOfPages = (float) newsService.getNewsItemsCountBySearch(search) / size;
        int maxPages = (int) Math.ceil(nrOfPages);
        model.addAttribute("maxPages", maxPages);

        return "newsList";
    }

    @PostMapping(value = "/newsList")
    public String searchNews(@ModelAttribute SearchDTO search) throws UnsupportedEncodingException {
        return "redirect:/newsList?search=" + URLEncoder.encode(search.getTitle(), "UTF-8");
    }
}
