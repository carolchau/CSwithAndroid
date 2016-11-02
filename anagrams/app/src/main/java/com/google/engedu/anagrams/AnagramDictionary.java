package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet wordSet = new HashSet();
    private HashMap <String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap <Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    // Constructor
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            String sortedWord = sortLetters(word);

            // Add words to HashMap lettersToWord
            if (lettersToWord.containsKey(sortedWord)) {
                // key exists; add word to the list that corresponds to the key
                lettersToWord.get(sortedWord).add(word);
            } else {
                // add sortedWord (key) & list containing word (value) to HashMap
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                lettersToWord.put(sortedWord, temp);
            }

            int wordLengthKey = word.length();

            // Add word length to HashMap sizeToWords
            if (sizeToWords.containsKey(wordLengthKey)) {
                // key exists; add word to list
                sizeToWords.get(wordLengthKey).add(word);
            } else {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                sizeToWords.put(wordLengthKey, temp);
            }
        }
    }

    // Returns true if word is in wordSet, and does not contain base word as substring
    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word) && !word.contains(base)) {
            return true;
        } else {
            return false;
        }
    }

    // Takes a string and finds all anagrams of that string
    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortLetters(targetWord);

        for (int i = 0; i < wordList.size(); i++) {

            if (sortedTargetWord.length() == wordList.get(i).length()) {
                if (sortedTargetWord.equals( sortLetters(wordList.get(i)) ) ) {
                    result.add(wordList.get(i));
                }
            }
        }

        return result;
    }

    // Finds all anagrams that can be formed by adding one more letter to base word
    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String temp = word + alphabet;
            String sortedWord = sortLetters(temp);

            // if word exists in HashMap, then add valid word list to the result
            if (lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> tempResult = lettersToWord.get(sortedWord);
                result.addAll(tempResult);
            }
        }

        return result;
    }

    // Returns a starting word that increases in length after each play
    public String pickGoodStarterWord() {
        ArrayList<String> goodWords = sizeToWords.get(wordLength);
        int index = random.nextInt(goodWords.size() - 1);

        String temp = goodWords.get(index);

        // Loop until it finds a word with at least MIN_NUM_ANAGRAMS possible answers
        while (getAnagramsWithOneMoreLetter(temp).size() < MIN_NUM_ANAGRAMS) {
            index = random.nextInt(goodWords.size() - 1);
            temp = goodWords.get(index);
        }

        // Increment wordLength unless it equals MAX_WORD_LENGTH
        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        return temp;
    }

    // Helper function that takes a string and returns the string in lexicographical ordering
    public String sortLetters(String wordToSort) {
        char[] chars = wordToSort.toCharArray();
        Arrays.sort(chars);
        String sortedWord = new String(chars);
        return sortedWord;
    }
}
