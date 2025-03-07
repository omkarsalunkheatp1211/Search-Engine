package com.searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.searchengine.model.WebPage;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Long> {
} 