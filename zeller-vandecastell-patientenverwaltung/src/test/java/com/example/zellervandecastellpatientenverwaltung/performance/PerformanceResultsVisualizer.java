package com.example.zellervandecastellpatientenverwaltung.performance;

import java.util.*;

/**
 * Visualisiert Performance-Test-Ergebnisse in Tabellenform
 */
public class PerformanceResultsVisualizer {

    private static final Map<String, Double> results = new LinkedHashMap<>();

    public static void addResult(String operation, double milliseconds) {
        results.put(operation, milliseconds);
    }

    public static void clear() {
        results.clear();
    }

    public static void printResults() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("MONGODB PERFORMANCE TEST RESULTS");
        System.out.println("=".repeat(80));
        System.out.println();

        // Header
        String header = String.format("| %-45s | %-15s |", "Operation", "MongoDB");
        String separator = "+" + "-".repeat(47) + "+" + "-".repeat(17) + "+";

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        // Daten
        for (Map.Entry<String, Double> entry : results.entrySet()) {
            String operation = entry.getKey();
            double ms = entry.getValue();
            String timeStr = formatTime(ms);

            String row = String.format("| %-45s | %-15s |", operation, timeStr);
            System.out.println(row);
            System.out.println(separator);
        }

        System.out.println();
    }

    private static String formatTime(double milliseconds) {
        if (milliseconds < 1) {
            return String.format("%.2fms", milliseconds);
        } else if (milliseconds < 1000) {
            return String.format("%.0fms", milliseconds);
        } else {
            return String.format("%.2fs", milliseconds / 1000.0);
        }
    }
}

