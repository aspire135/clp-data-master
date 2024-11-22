package com.yzu.clp.Util;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FilterWordUtil {

    public Map<String, Integer> filterKeywords(List<String> sentences, int topN) {
        // 创建Stanford CoreNLP管道
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // 统计关键词频率
        Map<String, Integer> keywordFreq = new HashMap<>();
        for (String sentence : sentences) {
            Annotation annotation = new Annotation(sentence);
            pipeline.annotate(annotation);
            List<CoreMap> sentencesList = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap coreMap : sentencesList) {
                List<String> words = new ArrayList<>();
                for (CoreMap token : coreMap.get(CoreAnnotations.TokensAnnotation.class)) {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    words.add(word);
                    keywordFreq.put(word, keywordFreq.getOrDefault(word, 0) + 1);
                }
                for (int i = 0; i < words.size(); i++) {
                    for (int j = i + 1; j <= words.size(); j++) {
                        List<String> phrase = words.subList(i, j);
                        String keyword = String.join(" ", phrase);
                        keywordFreq.put(keyword, keywordFreq.getOrDefault(keyword, 0) + 1);
                    }
                }
            }
        }

        // 根据关键词频率排序
        List<Map.Entry<String, Integer>> sortedKeywords = new ArrayList<>(keywordFreq.entrySet());
        sortedKeywords.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 获取前topN个关键词及其频率
        Map<String, Integer> topKeywords = new LinkedHashMap<>();
        for (int i = 0; i < topN && i < sortedKeywords.size(); i++) {
            Map.Entry<String, Integer> entry = sortedKeywords.get(i);
            topKeywords.put(entry.getKey(), entry.getValue());
        }

        return topKeywords;
    }
}
