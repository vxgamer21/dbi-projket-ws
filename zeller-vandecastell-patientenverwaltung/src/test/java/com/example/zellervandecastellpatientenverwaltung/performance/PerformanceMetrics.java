package com.example.zellervandecastellpatientenverwaltung.performance;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zum Sammeln und Auswerten von Performance-Metriken
 */
public class PerformanceMetrics {

    private final String operationName;
    private final List<Measurement> measurements;

    public PerformanceMetrics(String operationName) {
        this.operationName = operationName;
        this.measurements = new ArrayList<>();
    }

    /**
     * Fügt eine Messung hinzu
     */
    public void addMeasurement(int scale, long durationNanos, long itemCount) {
        measurements.add(new Measurement(scale, durationNanos, itemCount));
    }

    /**
     * Gibt eine formatierte Zusammenfassung aus
     */
    public void printSummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PERFORMANCE SUMMARY: " + operationName);
        System.out.println("=".repeat(80));
        System.out.printf("%-12s %-15s %-20s %-20s %-15s%n",
            "Skalierung", "Dauer (ms)", "Durchschnitt (ms)", "Items", "Throughput (ops/s)");
        System.out.println("-".repeat(80));

        for (Measurement m : measurements) {
            double durationMs = m.durationNanos / 1_000_000.0;
            double avgMs = m.itemCount > 0 ? durationMs / m.itemCount : 0;
            double throughput = durationMs > 0 ? (m.itemCount / durationMs) * 1000 : 0;

            System.out.printf("%-12d %-15.2f %-20.4f %-20d %-15.2f%n",
                m.scale, durationMs, avgMs, m.itemCount, throughput);
        }

        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Gibt CSV-formatierte Daten aus
     */
    public void printCSV() {
        System.out.println("\nCSV Format:");
        System.out.println("Operation,Scale,DurationMs,AvgMs,ItemCount,ThroughputOpsPerSec");

        for (Measurement m : measurements) {
            double durationMs = m.durationNanos / 1_000_000.0;
            double avgMs = m.itemCount > 0 ? durationMs / m.itemCount : 0;
            double throughput = durationMs > 0 ? (m.itemCount / durationMs) * 1000 : 0;

            System.out.printf("%s,%d,%.2f,%.4f,%d,%.2f%n",
                operationName, m.scale, durationMs, avgMs, m.itemCount, throughput);
        }
    }

    /**
     * Berechnet die durchschnittliche Dauer über alle Messungen
     */
    public double getAverageDuration() {
        if (measurements.isEmpty()) {
            return 0.0;
        }

        double totalDuration = measurements.stream()
            .mapToDouble(m -> m.durationNanos / 1_000_000.0)
            .sum();

        return totalDuration / measurements.size();
    }

    /**
     * Berechnet den durchschnittlichen Durchsatz
     */
    public double getAverageThroughput() {
        if (measurements.isEmpty()) {
            return 0.0;
        }

        double totalThroughput = measurements.stream()
            .mapToDouble(m -> {
                double durationMs = m.durationNanos / 1_000_000.0;
                return durationMs > 0 ? (m.itemCount / durationMs) * 1000 : 0;
            })
            .sum();

        return totalThroughput / measurements.size();
    }

    /**
     * Gibt Vergleichsstatistiken mit einer anderen Metrik aus
     */
    public void compareWith(PerformanceMetrics other) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("VERGLEICH: " + this.operationName + " vs " + other.operationName);
        System.out.println("=".repeat(80));

        double thisAvgDuration = this.getAverageDuration();
        double otherAvgDuration = other.getAverageDuration();
        double improvement = ((otherAvgDuration - thisAvgDuration) / otherAvgDuration) * 100;

        System.out.printf("%-40s: %.2f ms%n", this.operationName, thisAvgDuration);
        System.out.printf("%-40s: %.2f ms%n", other.operationName, otherAvgDuration);

        if (improvement > 0) {
            System.out.printf("Verbesserung: %.2f%% schneller%n", improvement);
        } else {
            System.out.printf("Verschlechterung: %.2f%% langsamer%n", -improvement);
        }

        double thisAvgThroughput = this.getAverageThroughput();
        double otherAvgThroughput = other.getAverageThroughput();
        double throughputImprovement = ((thisAvgThroughput - otherAvgThroughput) / otherAvgThroughput) * 100;

        System.out.printf("\n%-40s: %.2f ops/s%n", this.operationName + " Throughput", thisAvgThroughput);
        System.out.printf("%-40s: %.2f ops/s%n", other.operationName + " Throughput", otherAvgThroughput);

        if (throughputImprovement > 0) {
            System.out.printf("Throughput-Verbesserung: %.2f%%%n", throughputImprovement);
        } else {
            System.out.printf("Throughput-Verschlechterung: %.2f%%%n", -throughputImprovement);
        }

        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Exportiert die Metriken als JSON
     */
    public String toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"operation\": \"").append(operationName).append("\",\n");
        json.append("  \"measurements\": [\n");

        for (int i = 0; i < measurements.size(); i++) {
            Measurement m = measurements.get(i);
            double durationMs = m.durationNanos / 1_000_000.0;
            double avgMs = m.itemCount > 0 ? durationMs / m.itemCount : 0;
            double throughput = durationMs > 0 ? (m.itemCount / durationMs) * 1000 : 0;

            json.append("    {\n");
            json.append("      \"scale\": ").append(m.scale).append(",\n");
            json.append("      \"durationMs\": ").append(String.format("%.2f", durationMs)).append(",\n");
            json.append("      \"averageMs\": ").append(String.format("%.4f", avgMs)).append(",\n");
            json.append("      \"itemCount\": ").append(m.itemCount).append(",\n");
            json.append("      \"throughputOpsPerSec\": ").append(String.format("%.2f", throughput)).append("\n");
            json.append("    }");

            if (i < measurements.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ],\n");
        json.append("  \"summary\": {\n");
        json.append("    \"averageDurationMs\": ").append(String.format("%.2f", getAverageDuration())).append(",\n");
        json.append("    \"averageThroughput\": ").append(String.format("%.2f", getAverageThroughput())).append("\n");
        json.append("  }\n");
        json.append("}\n");

        return json.toString();
    }

    /**
     * Interne Klasse für einzelne Messungen
     */
    private static class Measurement {
        final int scale;
        final long durationNanos;
        final long itemCount;

        Measurement(int scale, long durationNanos, long itemCount) {
            this.scale = scale;
            this.durationNanos = durationNanos;
            this.itemCount = itemCount;
        }
    }

    /**
     * Factory-Methode zum Starten einer Zeitmessung
     */
    public static TimerContext startTimer() {
        return new TimerContext();
    }

    /**
     * Hilfsklasse für einfachere Zeitmessung
     */
    public static class TimerContext {
        private final long startTime;

        private TimerContext() {
            this.startTime = System.nanoTime();
        }

        public long stopAndGetNanos() {
            return System.nanoTime() - startTime;
        }

        public double stopAndGetMillis() {
            return (System.nanoTime() - startTime) / 1_000_000.0;
        }

        public void stopAndPrint(String operation, int itemCount) {
            double duration = stopAndGetMillis();
            double avg = itemCount > 0 ? duration / itemCount : 0;
            double throughput = duration > 0 ? (itemCount / duration) * 1000 : 0;

            System.out.printf("✓ %s: %d items in %.2f ms (%.4f ms/item, %.2f ops/s)%n",
                operation, itemCount, duration, avg, throughput);
        }
    }

    /**
     * Utility-Methode zum Formatieren von Zahlen mit Tausender-Trennzeichen
     */
    public static String formatNumber(long number) {
        return String.format("%,d", number);
    }

    /**
     * Utility-Methode zum Formatieren von Bytes in lesbare Größen
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
}

