package com.example.zellervandecastellpatientenverwaltung.performance;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Visualisiert MongoDB Performance Test Ergebnisse basierend auf den durchgeführten Tests
 */
public class MongoDBResultsTable {

    public static void main(String[] args) {
        Map<String, Double> mongoResults = new LinkedHashMap<>();

        // Ergebnisse aus den Tests (Durchschnittswerte)
        mongoResults.put("Create 10", 28.0);
        mongoResults.put("Create 100", 48.0);
        mongoResults.put("Create 1000", 126.0);
        mongoResults.put("Update", 122.0);
        mongoResults.put("Read Damage Filter With Projection", 80.0);
        mongoResults.put("Read Healthpoints Filter With Projection", 13.0);
        mongoResults.put("Read Fireresistance Filter With Projection", 15.0);
        mongoResults.put("Read Flying Filter With Projection", 2.0);
        mongoResults.put("Read Location Filter With Projection", 12.0);
        mongoResults.put("Read Reward Filter With Projection", 2.0);
        mongoResults.put("Read With Projection", 27.0);
        mongoResults.put("Aggregate Sort", 4.0);
        mongoResults.put("Aggregate Group By", 179.0);
        mongoResults.put("Aggregate Match", 31.0);
        mongoResults.put("Delete", 513.0);

        printTable(mongoResults);
    }

    private static void printTable(Map<String, Double> results) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("MONGODB PERFORMANCE TEST RESULTS");
        System.out.println("=".repeat(70));
        System.out.println();

        String separator = "+" + "-".repeat(47) + "+" + "-".repeat(20) + "+";
        String header = String.format("| %-45s | %-18s |", "", "MongoDB");

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        for (Map.Entry<String, Double> entry : results.entrySet()) {
            String operation = entry.getKey();
            double ms = entry.getValue();
            String timeStr = formatTime(ms);

            String row = String.format("| %-45s | %-18s |", operation, timeStr);
            System.out.println(row);
            System.out.println(separator);
        }

        System.out.println();
    }

    private static String formatTime(double milliseconds) {
        if (milliseconds < 1) {
            return String.format("%.2fms", milliseconds);
        } else {
            return String.format("%.0fms", milliseconds);
        }
    }
}

