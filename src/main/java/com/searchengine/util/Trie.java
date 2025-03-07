package com.searchengine.util;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;
        Map<Long, Integer> documentFrequency; // Maps document ID to frequency

        TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
            documentFrequency = new HashMap<>();
        }
    }

    public void insert(String word, Long documentId) {
        TrieNode current = root;
        word = word.toLowerCase();

        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }

        current.isEndOfWord = true;
        current.documentFrequency.merge(documentId, 1, Integer::sum);
    }

    public Map<Long, Integer> search(String word) {
        TrieNode node = searchNode(word.toLowerCase());
        return node != null ? node.documentFrequency : new HashMap<>();
    }

    private TrieNode searchNode(String word) {
        TrieNode current = root;

        for (char ch : word.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return null;
            }
            current = current.children.get(ch);
        }

        return current.isEndOfWord ? current : null;
    }
} 