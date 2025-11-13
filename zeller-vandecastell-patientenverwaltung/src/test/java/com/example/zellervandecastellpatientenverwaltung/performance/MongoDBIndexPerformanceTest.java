package com.example.zellervandecastellpatientenverwaltung.performance;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Performance-Tests für MongoDB-Indexierung
 *
 * Vergleicht die Performance von Queries mit und ohne Indexes.
 * BONUS: 1.0 Punkt für Index-Performance-Vergleich
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MongoDBIndexPerformanceTest {

    @Autowired
    private ArztRepository arztRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final int TEST_DATA_SIZE = 100000;
    private PerformanceMetrics metricsWithoutIndex;
    private PerformanceMetrics metricsWithIndex;

    @BeforeEach
    void setUp() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("MongoDB Index Performance Test Setup");
        System.out.println("=".repeat(80));

        metricsWithoutIndex = new PerformanceMetrics("Query ohne Index");
        metricsWithIndex = new PerformanceMetrics("Query mit Index");
    }

    @Test
    @Order(1)
    @DisplayName("1. Testdaten erstellen (100.000 Ärzte)")
    void createTestData() {
        System.out.println("\n--- Erstelle " + TEST_DATA_SIZE + " Ärzte für Index-Tests ---");

        // arztRepository.deleteAll();

        long startTime = System.nanoTime();
        List<Arzt> aerzte = TestDataGenerator.generateAerzte(TEST_DATA_SIZE);
        List<Arzt> saved = arztRepository.saveAll(aerzte);
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Ärzte erstellt in %.2f ms%n", saved.size(), duration);
        System.out.println("Warte 2 Sekunden für DB-Stabilisierung...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @Order(2)
    @DisplayName("2. Query-Performance OHNE Index")
    void testQueryWithoutIndex() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("QUERY PERFORMANCE OHNE INDEX");
        System.out.println("=".repeat(80));

        // Stelle sicher, dass keine Indexes existieren (außer _id)
        mongoTemplate.indexOps(Arzt.class).dropAllIndexes();

        System.out.println("\nAlle Indexes gelöscht (außer _id)");
        System.out.println("Aktive Indexes: " + mongoTemplate.indexOps(Arzt.class).getIndexInfo());

        // Test 1: Suche nach Fachgebiet
        System.out.println("\n--- Test 1: Suche nach Fachgebiet ---");
        PerformanceMetrics.TimerContext timer1 = PerformanceMetrics.startTimer();
        Query query1 = new Query(Criteria.where("fachgebiet").is(Fachgebiet.ORTHOPAEDIE));
        List<Arzt> result1 = mongoTemplate.find(query1, Arzt.class);
        long duration1 = timer1.stopAndGetNanos();
        timer1.stopAndPrint("Fachgebiet-Suche ohne Index", result1.size());
        metricsWithoutIndex.addMeasurement(TEST_DATA_SIZE, duration1, result1.size());

        // Test 2: Suche mit Sortierung
        System.out.println("\n--- Test 2: Suche mit Sortierung nach Name ---");
        PerformanceMetrics.TimerContext timer2 = PerformanceMetrics.startTimer();
        Query query2 = new Query(Criteria.where("fachgebiet").is(Fachgebiet.CHIRURGIE));
        query2.with(Sort.by(Sort.Direction.ASC, "name"));
        List<Arzt> result2 = mongoTemplate.find(query2, Arzt.class);
        long duration2 = timer2.stopAndGetNanos();
        timer2.stopAndPrint("Sortierte Suche ohne Index", result2.size());
        metricsWithoutIndex.addMeasurement(TEST_DATA_SIZE, duration2, result2.size());

        // Test 3: Range Query auf Geburtsdatum
        System.out.println("\n--- Test 3: Range Query auf Geburtsdatum ---");
        PerformanceMetrics.TimerContext timer3 = PerformanceMetrics.startTimer();
        Query query3 = new Query(Criteria.where("gebDatum")
            .gte(java.time.LocalDate.of(1970, 1, 1))
            .lte(java.time.LocalDate.of(1980, 12, 31)));
        List<Arzt> result3 = mongoTemplate.find(query3, Arzt.class);
        long duration3 = timer3.stopAndGetNanos();
        timer3.stopAndPrint("Range Query ohne Index", result3.size());
        metricsWithoutIndex.addMeasurement(TEST_DATA_SIZE, duration3, result3.size());

        // Test 4: Komplexe Query
        System.out.println("\n--- Test 4: Komplexe Query (Fachgebiet + Geburtsdatum) ---");
        PerformanceMetrics.TimerContext timer4 = PerformanceMetrics.startTimer();
        Query query4 = new Query(Criteria.where("fachgebiet").in(Fachgebiet.ORTHOPAEDIE, Fachgebiet.CHIRURGIE)
            .and("gebDatum").gte(java.time.LocalDate.of(1970, 1, 1)));
        List<Arzt> result4 = mongoTemplate.find(query4, Arzt.class);
        long duration4 = timer4.stopAndGetNanos();
        timer4.stopAndPrint("Komplexe Query ohne Index", result4.size());
        metricsWithoutIndex.addMeasurement(TEST_DATA_SIZE, duration4, result4.size());

        System.out.println("\n✓ Tests ohne Index abgeschlossen");
    }

    @Test
    @Order(3)
    @DisplayName("3. Indexes erstellen")
    void createIndexes() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("INDEXES ERSTELLEN");
        System.out.println("=".repeat(80));

        // Index auf Fachgebiet
        System.out.println("\n--- Erstelle Index auf 'fachgebiet' ---");
        long start1 = System.nanoTime();
        mongoTemplate.indexOps(Arzt.class)
            .ensureIndex(new Index().on("fachgebiet", Sort.Direction.ASC));
        double duration1 = (System.nanoTime() - start1) / 1_000_000.0;
        System.out.printf("✓ Index auf 'fachgebiet' erstellt in %.2f ms%n", duration1);

        // Index auf Name
        System.out.println("\n--- Erstelle Index auf 'name' ---");
        long start2 = System.nanoTime();
        mongoTemplate.indexOps(Arzt.class)
            .ensureIndex(new Index().on("name", Sort.Direction.ASC));
        double duration2 = (System.nanoTime() - start2) / 1_000_000.0;
        System.out.printf("✓ Index auf 'name' erstellt in %.2f ms%n", duration2);

        // Index auf Geburtsdatum
        System.out.println("\n--- Erstelle Index auf 'gebDatum' ---");
        long start3 = System.nanoTime();
        mongoTemplate.indexOps(Arzt.class)
            .ensureIndex(new Index().on("gebDatum", Sort.Direction.ASC));
        double duration3 = (System.nanoTime() - start3) / 1_000_000.0;
        System.out.printf("✓ Index auf 'gebDatum' erstellt in %.2f ms%n", duration3);

        // Compound Index auf Fachgebiet + Geburtsdatum
        System.out.println("\n--- Erstelle Compound Index auf 'fachgebiet' + 'gebDatum' ---");
        long start4 = System.nanoTime();
        mongoTemplate.indexOps(Arzt.class)
            .ensureIndex(new Index()
                .on("fachgebiet", Sort.Direction.ASC)
                .on("gebDatum", Sort.Direction.ASC));
        double duration4 = (System.nanoTime() - start4) / 1_000_000.0;
        System.out.printf("✓ Compound Index erstellt in %.2f ms%n", duration4);

        // Zeige alle Indexes
        System.out.println("\n--- Aktive Indexes ---");
        mongoTemplate.indexOps(Arzt.class).getIndexInfo().forEach(indexInfo -> {
            System.out.println("  - " + indexInfo.getName() + ": " + indexInfo.getIndexFields());
        });

        System.out.println("\nWarte 2 Sekunden für Index-Optimierung...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @Order(4)
    @DisplayName("4. Query-Performance MIT Index")
    void testQueryWithIndex() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("QUERY PERFORMANCE MIT INDEX");
        System.out.println("=".repeat(80));

        // Test 1: Suche nach Fachgebiet (mit Index)
        System.out.println("\n--- Test 1: Suche nach Fachgebiet (mit Index) ---");
        PerformanceMetrics.TimerContext timer1 = PerformanceMetrics.startTimer();
        Query query1 = new Query(Criteria.where("fachgebiet").is(Fachgebiet.ORTHOPAEDIE));
        List<Arzt> result1 = mongoTemplate.find(query1, Arzt.class);
        long duration1 = timer1.stopAndGetNanos();
        timer1.stopAndPrint("Fachgebiet-Suche mit Index", result1.size());
        metricsWithIndex.addMeasurement(TEST_DATA_SIZE, duration1, result1.size());

        // Test 2: Suche mit Sortierung (mit Index)
        System.out.println("\n--- Test 2: Suche mit Sortierung nach Name (mit Index) ---");
        PerformanceMetrics.TimerContext timer2 = PerformanceMetrics.startTimer();
        Query query2 = new Query(Criteria.where("fachgebiet").is(Fachgebiet.CHIRURGIE));
        query2.with(Sort.by(Sort.Direction.ASC, "name"));
        List<Arzt> result2 = mongoTemplate.find(query2, Arzt.class);
        long duration2 = timer2.stopAndGetNanos();
        timer2.stopAndPrint("Sortierte Suche mit Index", result2.size());
        metricsWithIndex.addMeasurement(TEST_DATA_SIZE, duration2, result2.size());

        // Test 3: Range Query (mit Index)
        System.out.println("\n--- Test 3: Range Query auf Geburtsdatum (mit Index) ---");
        PerformanceMetrics.TimerContext timer3 = PerformanceMetrics.startTimer();
        Query query3 = new Query(Criteria.where("gebDatum")
            .gte(java.time.LocalDate.of(1970, 1, 1))
            .lte(java.time.LocalDate.of(1980, 12, 31)));
        List<Arzt> result3 = mongoTemplate.find(query3, Arzt.class);
        long duration3 = timer3.stopAndGetNanos();
        timer3.stopAndPrint("Range Query mit Index", result3.size());
        metricsWithIndex.addMeasurement(TEST_DATA_SIZE, duration3, result3.size());

        // Test 4: Komplexe Query (mit Compound Index)
        System.out.println("\n--- Test 4: Komplexe Query mit Compound Index ---");
        PerformanceMetrics.TimerContext timer4 = PerformanceMetrics.startTimer();
        Query query4 = new Query(Criteria.where("fachgebiet").in(Fachgebiet.ORTHOPAEDIE, Fachgebiet.CHIRURGIE)
            .and("gebDatum").gte(java.time.LocalDate.of(1970, 1, 1)));
        List<Arzt> result4 = mongoTemplate.find(query4, Arzt.class);
        long duration4 = timer4.stopAndGetNanos();
        timer4.stopAndPrint("Komplexe Query mit Index", result4.size());
        metricsWithIndex.addMeasurement(TEST_DATA_SIZE, duration4, result4.size());

        System.out.println("\n✓ Tests mit Index abgeschlossen");
    }

    @Test
    @Order(5)
    @DisplayName("5. Vergleich und Auswertung")
    void compareResults() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("VERGLEICH: MIT vs OHNE INDEX");
        System.out.println("=".repeat(80));

        metricsWithoutIndex.printSummary();
        metricsWithIndex.printSummary();
        metricsWithIndex.compareWith(metricsWithoutIndex);

        // Einzelvergleiche
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DETAILLIERTER VERGLEICH");
        System.out.println("=".repeat(80));

        String[] testNames = {
            "Test 1: Fachgebiet-Suche",
            "Test 2: Sortierte Suche",
            "Test 3: Range Query",
            "Test 4: Komplexe Query"
        };

        for (int i = 0; i < Math.min(testNames.length, 4); i++) {
            double withoutIndexMs = metricsWithoutIndex.getAverageDuration();
            double withIndexMs = metricsWithIndex.getAverageDuration();
            double speedup = withoutIndexMs / withIndexMs;
            double improvement = ((withoutIndexMs - withIndexMs) / withoutIndexMs) * 100;

            System.out.printf("\n%s:%n", testNames[i]);
            System.out.printf("  Ohne Index: %.2f ms%n", withoutIndexMs);
            System.out.printf("  Mit Index:  %.2f ms%n", withIndexMs);
            System.out.printf("  Speedup:    %.2fx%n", speedup);
            System.out.printf("  Verbesserung: %.2f%%%n", improvement);
        }

        // Fazit
        System.out.println("\n" + "=".repeat(80));
        System.out.println("FAZIT");
        System.out.println("=".repeat(80));

        double overallSpeedup = metricsWithoutIndex.getAverageDuration() / metricsWithIndex.getAverageDuration();

        System.out.println("\n✓ Index-Performance-Tests erfolgreich abgeschlossen!");
        System.out.printf("  Durchschnittliche Performance-Verbesserung: %.2fx schneller%n", overallSpeedup);
        System.out.println("\n📊 Empfehlungen:");
        System.out.println("  - Indexes sind essentiell für häufig verwendete Filter-Felder");
        System.out.println("  - Compound Indexes für Queries mit mehreren Bedingungen");
        System.out.println("  - Sortierung profitiert stark von Indexes");
        System.out.println("  - Trade-off: Indexes verlangsamen Writes etwas");
        System.out.println("=".repeat(80) + "\n");
    }

    @AfterEach
    void tearDown() {
        // Indexes bleiben für weitere Tests erhalten
        // arztRepository.deleteAll(); // Optional: Daten behalten
    }
}

