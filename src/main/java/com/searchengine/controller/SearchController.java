package com.searchengine.controller;

import com.searchengine.model.WebPage;
import com.searchengine.model.SearchHistory;
import com.searchengine.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<WebPage>> search(@RequestParam String query) {
        return ResponseEntity.ok(searchService.search(query));
    }

    @GetMapping("/history")
    public ResponseEntity<List<SearchHistory>> getSearchHistory() {
        return ResponseEntity.ok(searchService.getSearchHistory());
    }

    @PostMapping("/history")
    public ResponseEntity<Void> addToHistory(@RequestBody SearchHistory history) {
        searchService.addToHistory(history);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        searchService.clearHistory();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistoryItem(@PathVariable Long id) {
        searchService.deleteHistoryItem(id);
        return ResponseEntity.ok().build();
    }
} 