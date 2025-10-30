package com.example.zellervandecastellpatientenverwaltung.foundation;

public class Base58 {
    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final int BASE = ALPHABET.length;

    public static String encode(byte[] input) {
        StringBuilder sb = new StringBuilder();
        int leadingZeroes = 0;
        for (byte b : input) {
            if (b == 0) {
                leadingZeroes++;
            } else {
                break;
            }
        }

        int num = 0;
        for (byte b : input) {
            num = (num << 8) | (b & 0xFF);
        }
        while (num > 0) {
            sb.insert(0, ALPHABET[num % BASE]);
            num /= BASE;
        }

        while (leadingZeroes-- > 0) {
            sb.insert(0, ALPHABET[0]);
        }
        return sb.toString();
    }
}
