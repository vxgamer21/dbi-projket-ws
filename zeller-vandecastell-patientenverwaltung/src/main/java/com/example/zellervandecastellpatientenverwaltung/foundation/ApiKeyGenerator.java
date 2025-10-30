package com.example.zellervandecastellpatientenverwaltung.foundation;

import java.security.SecureRandom;

public class ApiKeyGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateApiKey() {
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        return Base58.encode(randomBytes);
    }

    public static void main(String[] args) {
        System.out.println("Generated API Key: " + generateApiKey());
    }
}
