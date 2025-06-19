package com.cezar.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class CezarUtils {
    private static final String ENG_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String UKR_ALPHABET = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя";
    private static final String ADDITIONAL_SYMBOLS = " .,«»\"':!?";

    public static String encrypt(String text, int key) {
        String alphabet = detectAlphabet(text);
        return shift(text, key, alphabet);
    }

    public static String decrypt(String text, int key) {
        String alphabet = detectAlphabet(text);
        return shift(text, -key, alphabet);
    }

    public static String bruteForce(String cipher, String frequencySample) {
        String alphabet = detectAlphabet(cipher + frequencySample);
        double bestScore = Double.MAX_VALUE;
        int bestKey = 0;

        for (int k = 1; k < alphabet.length(); k++) {
            String attempt = shift(cipher, -k, alphabet);
            double score = chiSquaredScore(attempt, frequencySample, alphabet);
            if (score < bestScore) {
                bestScore = score;
                bestKey = k;
            }
        }

        return "Best key: " + bestKey + "\n" + decrypt(cipher, bestKey);
    }

    public static String bruteForce(String cipher) {
        String alphabet = detectAlphabet(cipher);
        StringBuilder results = new StringBuilder();

        for (int key = 1; key < alphabet.length(); key++) {
            String attempt = shift(cipher, -key, alphabet);
            results.append("Key ").append(key).append(": ").append(attempt).append("\n");
        }

        return results.toString();
    }

    private static double chiSquaredScore(String candidate, String reference, String alphabet) {
        int[] freq1 = frequency(candidate, alphabet);
        int[] freq2 = frequency(reference, alphabet);

        int total1 = Arrays.stream(freq1).sum();
        int total2 = Arrays.stream(freq2).sum();
        double score = 0;

        for (int i = 0; i < alphabet.length(); i++) {
            double expected = (double) freq2[i] / total2 * total1 + 1e-6;
            score += Math.pow(freq1[i] - expected, 2) / expected;
        }

        return score;
    }

    private static int[] frequency(String text, String alphabet) {
        int[] freq = new int[alphabet.length()];
        for (char ch : text.toLowerCase().toCharArray()) {
            if (!Character.isLetter(ch)) continue; // 💡 Ігнорувати не-літери
            int idx = alphabet.indexOf(ch);
            if (idx >= 0) freq[idx]++;
        }
        return freq;
    }

    private static String shift(String text, int key, String alphabet) {
        int len = alphabet.length();
        key = ((key % len) + len) % len;

        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            int idx = alphabet.indexOf(Character.toLowerCase(ch));
            if (idx == -1) {
                result.append(ch);
            } else {
                char shifted = alphabet.charAt((idx + key) % len);
                if (Character.isUpperCase(ch)) {
                    result.append(Character.toUpperCase(shifted));
                } else {
                    result.append(shifted);
                }
            }
        }
        return result.toString();
    }

    private static String detectAlphabet(String text) {
        for (char c : text.toLowerCase().toCharArray()) {
            if (UKR_ALPHABET.indexOf(c) != -1)
                return UKR_ALPHABET + ADDITIONAL_SYMBOLS;
        }
        return ENG_ALPHABET + ADDITIONAL_SYMBOLS;
    }

    public static void process(String filePath, int key, boolean encrypt) {
        try {
            Path path = Path.of(filePath);
            String content = FileUtils.read(path);
            String result = encrypt ? encrypt(content, key) : decrypt(content, key);
            String suffix = encrypt ? "[ENCRYPTED]" : "[DECRYPTED]";
            Path newPath = path.resolveSibling(path.getFileName().toString().replace(".txt", "") + suffix + ".txt");
            FileUtils.write(newPath, result);
            System.out.println("See your file by path: " + newPath);
        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        }
    }
}
