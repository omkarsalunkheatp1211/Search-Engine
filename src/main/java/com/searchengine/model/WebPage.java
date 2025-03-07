package com.searchengine.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "web_pages")
public class WebPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String url;

    @Column(length = 10000)
    private String title;

    @Column(length = 100000)
    private String content;

    private double pageRank = 1.0;
    
    private LocalDateTime lastCrawled;

    @Column(length = 1000)
    private String description;

    private int inboundLinks = 0;
    private int outboundLinks = 0;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPageRank() {
        return pageRank;
    }

    public void setPageRank(double pageRank) {
        this.pageRank = pageRank;
    }

    public LocalDateTime getLastCrawled() {
        return lastCrawled;
    }

    public void setLastCrawled(LocalDateTime lastCrawled) {
        this.lastCrawled = lastCrawled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInboundLinks() {
        return inboundLinks;
    }

    public void setInboundLinks(int inboundLinks) {
        this.inboundLinks = inboundLinks;
    }

    public int getOutboundLinks() {
        return outboundLinks;
    }

    public void setOutboundLinks(int outboundLinks) {
        this.outboundLinks = outboundLinks;
    }

    // Default constructor
    public WebPage() {
        this.pageRank = 1.0;
        this.inboundLinks = 0;
        this.outboundLinks = 0;
        this.lastCrawled = LocalDateTime.now();
    }
} 