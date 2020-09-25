package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = "";

        try {
            text = new String(Files.readAllBytes(Paths.get(args[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] sentences = text.split("[.?!]");
        Words words = new Words(text.split("[\\s]"));


        System.out.println("The text is:");
        System.out.println(text + '\n');
        System.out.println("Words: " + words.getWords());
        System.out.println("Sentences: " + sentences.length);
        System.out.println("Characters: " + words.getCharacters());
        System.out.println("Syllables: " + words.getSyllables());
        System.out.println("Polysyllables: " + words.getPolysyllables());
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        String scoreType = scanner.next();
        System.out.println();
        switch (scoreType) {
            case "ARI" :
                System.out.println(automated(words.getWords(), sentences.length, words.getCharacters()));
                break;
            case "FK" :
                System.out.println(fleschKincaid(words.getWords(), sentences.length, words.getSyllables()));
                break;
            case "SMOG" :
                System.out.println(simpleMeasure(sentences.length, words.getPolysyllables()));
                break;
            case "CL" :
                System.out.println(colemanLiau(words.getWords(), sentences.length, words.getCharacters()));
                break;
            case "all" :
                System.out.println(automated(words.getWords(), sentences.length, words.getCharacters()));
                System.out.println(fleschKincaid(words.getWords(), sentences.length, words.getSyllables()));
                System.out.println(simpleMeasure(sentences.length, words.getPolysyllables()));
                System.out.println(colemanLiau(words.getWords(), sentences.length, words.getCharacters()));
                break;
        }

    }

    static String automated(int words, int sentences, int characters) {
        double score = (4.71 * characters) / words + (0.5 * words) / sentences - 21.43;

        return String.format("Automated Readability Index: %.2f (about  ", score) + gradeLevel((int) Math.ceil(score)) +
                " year olds).";
    }

    static String fleschKincaid(int words, int sentences, int syllables) {
        double score = (0.39 * words) / sentences + (11.8 * syllables) / words - 15.59;

        return String.format("Flesch–Kincaid readability tests: %.2f (about  ", score) + gradeLevel((int) Math.ceil(score)) +
                " year olds).";
    }

    static String simpleMeasure(int sentences, int polysyllables) {
        double score = 1.043 * Math.sqrt((polysyllables * 30.0) / sentences) + 3.1291;

        return String.format("Simple Measure of Gobbledygook: %.2f (about  ", score) + gradeLevel((int) Math.ceil(score)) +
                " year olds).";
    }

    static String colemanLiau(int words, int sentences, int characters) {
        double score = 5.88 * characters / words - 29.6 * sentences / words - 15.8;

        return String.format("Coleman–Liau index: %.2f (about  ", score) + gradeLevel((int) Math.ceil(score)) +
                " year olds).";
    }

    static String gradeLevel(int score) {
        if (score < 3) {
           return String.valueOf(5 + score);
        } else if (score < 13) {
            return String.valueOf(6 + score);
        } else if (score == 13) {
            return String.valueOf(24);
        } else {
            return "24+";
        }

    }
}

class Words {
    private int words;
    private int syllables;
    private int polysyllables;

    public int getWords() {
        return words;
    }

    public int getSyllables() {
        return syllables;
    }

    public int getPolysyllables() {
        return polysyllables;
    }

    public int getCharacters() {
        return characters;
    }

    private int characters;

    public Words(String[] wordsString) {
        this.words = wordsString.length;
        this.characters = 0;
        this.syllables = 0;
        this.polysyllables = 0;
        for (String word : wordsString) {
            characters +=  word.length();
            int syllablesInWord = countSyllables(word);
            syllables += syllablesInWord;
            if (syllablesInWord > 2) {
                polysyllables++;
            }
        }
    }

    private int countSyllables(String word) {
        int syllables = 0;
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i))) {
                while (i < word.length() - 1) {
                    if (isVowel(word.charAt(i + 1))) {
                        i++;
                    } else {
                        break;
                    }
                }
                syllables++;
            }
        }
        if (word.matches(".*[eE][.?!]?")) {
            syllables--;
        }
        if (syllables == 0)
            return 1;
        return syllables;
    }

    private boolean isVowel(char c) {
        return String.valueOf(c).matches("[aeoiuyAEOIUY]");
    }
}
