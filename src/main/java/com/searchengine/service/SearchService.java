package com.searchengine.service;

import com.searchengine.model.WebPage;
import com.searchengine.model.SearchHistory;
import com.searchengine.repository.WebPageRepository;
import com.searchengine.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final WebPageRepository webPageRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private static final int MAX_RESULTS = 10;
    private static final String SEARCH_URL = "https://www.bing.com/search";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    public List<WebPage> search(String query) {
        List<WebPage> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        try {
            String encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8.toString());
            String searchUrl = SEARCH_URL + "?q=" + encodedQuery;
            
            log.info("Searching with URL: {}", searchUrl);
            
            Document doc = Jsoup.connect(searchUrl)
                    .userAgent(USER_AGENT)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .timeout(10000)
                    .get();

            // Try multiple selectors to find search results
            Elements searchResults = doc.select("li.b_algo");
            if (searchResults.isEmpty()) {
                searchResults = doc.select("div.b_algo"); // Alternative class
            }
            if (searchResults.isEmpty()) {
                searchResults = doc.select("[class*=algo]"); // Any element with 'algo' in class name
            }

            log.info("Found {} raw search results", searchResults.size());

            for (Element result : searchResults) {
                Element titleElement = result.selectFirst("h2");
                Element linkElement = result.selectFirst("a");
                Element snippetElement = result.selectFirst("div.b_caption p, div[class*=caption] p, div[class*=desc]");

                if (titleElement != null && linkElement != null) {
                    WebPage webPage = new WebPage();
                    webPage.setTitle(titleElement.text());
                    webPage.setUrl(linkElement.attr("href"));
                    webPage.setDescription(snippetElement != null ? snippetElement.text() : "");
                    
                    try {
                        webPageRepository.save(webPage);
                        results.add(webPage);
                    } catch (Exception e) {
                        log.error("Error saving webpage: ", e);
                        results.add(webPage);
                    }

                    if (results.size() >= MAX_RESULTS) {
                        break;
                    }
                }
            }

            log.info("Returning {} processed search results", results.size());
        } catch (IOException e) {
            log.error("Error during search: ", e);
        }
        
        return results;
    }

    @Transactional
    public void addToHistory(SearchHistory history) {
        try {
            if (history == null) {
                throw new IllegalArgumentException("History object cannot be null");
            }
            
            // Set current time if not provided
            if (history.getVisitTime() == null) {
                history.setVisitTime(LocalDateTime.now());
            }
            
            log.info("Saving search history: {}", history);
            searchHistoryRepository.save(history);
            log.info("Successfully saved search history");
        } catch (Exception e) {
            log.error("Error saving search history: ", e);
            throw e;
        }
    }

    public List<SearchHistory> getSearchHistory() {
        try {
            log.info("Fetching search history");
            List<SearchHistory> history = searchHistoryRepository.findTop10ByOrderByVisitTimeDesc();
            log.info("Found {} history items", history.size());
            return history;
        } catch (Exception e) {
            log.error("Error fetching search history: ", e);
            throw e;
        }
    }

    @Transactional
    public void clearHistory() {
        try {
            log.info("Clearing all search history");
            searchHistoryRepository.deleteAll();
            log.info("Successfully cleared search history");
        } catch (Exception e) {
            log.error("Error clearing search history: ", e);
            throw e;
        }
    }

    @Transactional
    public void deleteHistoryItem(Long id) {
        try {
            log.info("Deleting history item with id: {}", id);
            searchHistoryRepository.deleteById(id);
            log.info("Successfully deleted history item");
        } catch (Exception e) {
            log.error("Error deleting history item: ", e);
            throw e;
        }
    }
}