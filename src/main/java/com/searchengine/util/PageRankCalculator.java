package com.searchengine.util;

import com.searchengine.model.WebPage;
import java.util.*;

public class PageRankCalculator {
    private static final double DAMPING_FACTOR = 0.85;
    private static final double EPSILON = 0.0001;
    private static final int MAX_ITERATIONS = 100;

    public Map<Long, Double> calculatePageRanks(List<WebPage> pages, Map<Long, Set<Long>> inboundLinks) {
        Map<Long, Double> pageRanks = new HashMap<>();
        int numPages = pages.size();
        
        // Initialize PageRank scores
        for (WebPage page : pages) {
            pageRanks.put(page.getId(), 1.0);
        }

        // Iterate until convergence
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            Map<Long, Double> newPageRanks = new HashMap<>();
            double maxDiff = 0.0;

            for (WebPage page : pages) {
                Long pageId = page.getId();
                Set<Long> inbound = inboundLinks.getOrDefault(pageId, new HashSet<>());
                
                double sum = 0.0;
                for (Long inboundId : inbound) {
                    WebPage inboundPage = pages.stream()
                            .filter(p -> p.getId().equals(inboundId))
                            .findFirst()
                            .orElse(null);
                    
                    if (inboundPage != null) {
                        sum += pageRanks.get(inboundId) / inboundPage.getOutboundLinks();
                    }
                }

                double newRank = (1 - DAMPING_FACTOR) + DAMPING_FACTOR * sum;
                newPageRanks.put(pageId, newRank);
                
                maxDiff = Math.max(maxDiff, Math.abs(newRank - pageRanks.get(pageId)));
            }

            pageRanks = newPageRanks;
            
            // Check for convergence
            if (maxDiff < EPSILON) {
                break;
            }
        }

        return pageRanks;
    }

    public void normalizePageRanks(Map<Long, Double> pageRanks) {
        double maxRank = pageRanks.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
        pageRanks.forEach((id, rank) -> pageRanks.put(id, rank / maxRank));
    }
} 