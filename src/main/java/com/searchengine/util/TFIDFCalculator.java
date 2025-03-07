package com.searchengine.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFCalculator {
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (word.equalsIgnoreCase(term)) {
                result++;
            }
        }
        return result / doc.size();
    }

    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (word.equalsIgnoreCase(term)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / (n + 1));
    }

    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }

    public Map<String, Double> calculateDocumentScores(List<String> searchTerms, List<List<String>> documents) {
        Map<String, Double> scores = new HashMap<>();
        
        for (int i = 0; i < documents.size(); i++) {
            List<String> doc = documents.get(i);
            double score = 0;
            
            for (String term : searchTerms) {
                score += tfIdf(doc, documents, term);
            }
            
            scores.put("doc" + i, score);
        }
        
        return scores;
    }
} 