package com.searchengine.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "search_history")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime visitTime;

    @Column(nullable = false)
    private boolean isSearchQuery = false;
} 