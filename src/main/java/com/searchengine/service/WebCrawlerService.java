package com.searchengine.service;

import com.searchengine.model.WebPage;
import com.searchengine.repository.WebPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebCrawlerService {
    private final WebPageRepository webPageRepository;
    private final Set<String> visitedUrls = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final int MAX_DEPTH = 3;
    private static final int TIMEOUT = 10000;

    public void crawl(String seedUrl) {
        crawlPage(seedUrl, 0);
    }

    private void crawlPage(String url, int depth) {
        if (depth >= MAX_DEPTH || !visitedUrls.add(url)) {
            return;
        }

        try {
            Document doc = Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .userAgent("SearchEngineBot/1.0")
                    .get();

            WebPage webPage = new WebPage();
            webPage.setUrl(url);
            webPage.setTitle(doc.title());
            webPage.setContent(doc.text());
            webPage.setDescription(getMetaDescription(doc));
            webPage.setLastCrawled(LocalDateTime.now());

            Elements links = doc.select("a[href]");
            webPage.setOutboundLinks(links.size());

            webPageRepository.save(webPage);

            for (Element link : links) {
                String nextUrl = link.attr("abs:href");
                if (isValidUrl(nextUrl)) {
                    crawlPage(nextUrl, depth + 1);
                }
            }
        } catch (IOException e) {
            log.error("Error crawling URL: " + url, e);
        }
    }

    private String getMetaDescription(Document doc) {
        Element descriptionMeta = doc.select("meta[name=description]").first();
        return descriptionMeta != null ? descriptionMeta.attr("content") : "";
    }

    private boolean isValidUrl(String url) {
        return url != null && !url.isEmpty() && 
               (url.startsWith("http://") || url.startsWith("https://")) &&
               !url.contains("#") && !url.endsWith(".pdf") && 
               !url.endsWith(".jpg") && !url.endsWith(".png");
    }
} 