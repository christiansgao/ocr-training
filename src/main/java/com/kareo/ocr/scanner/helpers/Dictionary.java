package com.kareo.ocr.scanner.helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Dictionary {
    private Set<String> wordsSet;

    public Dictionary() {
        wordsSet = new HashSet<String>();
        try {
            String[] wordList = {"medicalwords.txt", "allwords.txt", "mywords.txt"};
            for (String word : wordList) {
                Path path = Paths.get("data/words/" + word);
                byte[] readBytes = Files.readAllBytes(path);
                String wordListContents = new String(readBytes, "UTF-8");
                wordListContents = wordListContents.replaceAll("\r", "");
                String[] words = wordListContents.split("\n");
                Collections.addAll(wordsSet, words);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        Dictionary dict = new Dictionary();

        System.out.println(dict.validWord("medical"));
        System.out.println(dict.validWord("chicken??"));
        System.out.println(dict.validWord("rx "));
        System.out.println(dict.validWord("hsa"));
        System.out.println(dict.validWord("pharm!@#!acy"));

        String tst_str = "medical life   ----cat--- ???!@## wtf hi insurance";
        System.out.println("valid words: " + dict.validWords(tst_str));

    }

    public boolean validWord(String word) {
        if (word.length() == 1)
            return false;
        else
            word = word; //.replaceAll("[^a-z]", "");

        return wordsSet.contains(word);
    }

    public List<String> validWords(String words_str) {

        List<String> valid_words = new ArrayList<String>();
        String[] words = words_str.toLowerCase().trim().replaceAll(" +", " ").split(" ");

        for (String word : words) {
            if (validWord(word))
                valid_words.add(word);
        }

        return valid_words;
    }
}
