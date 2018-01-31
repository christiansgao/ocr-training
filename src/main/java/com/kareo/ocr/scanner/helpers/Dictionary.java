package com.kareo.ocr.scanner.helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Dictionary {
    private Set<String> wordsSet;
    private Set<String> insuranceSet;

    public class WordResults{
        public List<String> valid_words = new ArrayList<String>();
        public List<String> invalid_words = new ArrayList<String>();
    }

    public Dictionary() {
        wordsSet = new HashSet<String>();
        insuranceSet = new HashSet<String>();

        try {
            String[] wordList = {"medicalwords.txt", "allwords.txt", "mywords.txt"};
            for (String word : wordList) {
                Path path = Paths.get("data/words/" + word);
                byte[] readBytes = Files.readAllBytes(path);
                String wordListContents = new String(readBytes, "UTF-8");
                wordListContents = wordListContents.replaceAll("\r", "");
                String[] words = wordListContents.split("\n");
                Collections.addAll(wordsSet, words);
                if(word.equals("mywords.txt")){
                    Collections.addAll(insuranceSet, words);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        Dictionary dict = new Dictionary();

        System.out.println(dict.isValidWord("medical"));
        System.out.println(dict.isValidWord("chicken??"));
        System.out.println(dict.isValidWord("rx "));
        System.out.println(dict.isValidWord("hsa"));
        System.out.println(dict.isValidWord("pharm!@#!acy"));

        String tst_str = "medical life   ----cat--- ???!@## wtf hi insurance";
        System.out.println("valid words: " + dict.validWords(tst_str));

    }

    public boolean isValidWord(String word) {
        if (word.length() == 1)
            return false;
        else
            word = word; //.replaceAll("[^a-z]", "");

        return wordsSet.contains(word);
    }

    public WordResults validWords(String words_str) {
        WordResults results = new WordResults();
        List<String> valid_words = new ArrayList<String>();
        String[] words = words_str.toLowerCase().trim().replaceAll(" +", " ").split(" ");

        for (String word : words) {
            if (isValidWord(word))
                results.valid_words.add(word);
            else
                results.invalid_words.add(word);

        }
        return results;
    }

    public boolean isInsuranceWord(String word) {
        return insuranceSet.contains(word);
    }
}
