package com.example.zellervandecastellpatientenverwaltung.performance;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungReferencedRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Performance-Vergleich: Referencing (@DBRef) vs Embedding
 *
 * BONUS-AUFGABE: 1.0 Punkt
 *
 * Dieser Test vergleicht zwei MongoDB-Modellierungsansätze:
 *
 * 1. EMBEDDING (Behandlung):
 *    - Arzt und Patient werden vollständig eingebettet
 *    - Schnellere Reads (alles in einem Dokument)
 *    - Redundanz bei Updates
 *
 * 2. REFERENCING (BehandlungReferenced):
 *    - Nur ObjectIds werden gespeichert (@DBRef)
 *    - Langsamere Reads (zusätzliche Queries nötig)
 *    - Konsistente Daten ohne Redundanz
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReferencingVsEmbeddingPerformanceTest {

    @Autowired
    private ArztRepository arztRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BehandlungRepository behandlungRepository; // Embedded

    @Autowired
    private BehandlungReferencedRepository behandlungReferencedRepository; // Referenced

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final int TEST_DATA_SIZE = 10000;
    private static final Random random = new Random();

    private static final List<Arzt> aerzte = new ArrayList<>();
    private static final List<Patient> patienten = new ArrayList<>();

    private static final java.util.LinkedHashMap<String, Double> performanceResults = new java.util.LinkedHashMap<>();

    @BeforeAll
    static void setUpAll(@Autowired ArztRepository arztRepository,
                         @Autowired PatientRepository patientRepository,
                         @Autowired BehandlungRepository behandlungRepository,
                         @Autowired BehandlungReferencedRepository behandlungReferencedRepository) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SETUP: Erstelle Testdaten (Ärzte und Patienten)");
        System.out.println("=".repeat(80));

        // Cleanup
        arztRepository.deleteAll();
        patientRepository.deleteAll();
        behandlungRepository.deleteAll();
        behandlungReferencedRepository.deleteAll();
        aerzte.clear();
        patienten.clear();

        // Ärzte erstellen
        System.out.printf("Erstelle %d Ärzte...%n", 100);
        List<Arzt> aerzteListe = TestDataGenerator.generateAerzte(100);
        aerzte.addAll(arztRepository.saveAll(aerzteListe));

        // Patienten erstellen
        System.out.printf("Erstelle %d Patienten...%n", 100);
        List<Patient> patientenListe = TestDataGenerator.generatePatienten(100);
        patienten.addAll(patientRepository.saveAll(patientenListe));

        System.out.printf("✓ Testdaten bereit: %d Ärzte, %d Patienten%n", aerzte.size(), patienten.size());
    }

    @BeforeEach
    void setUp() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Referencing vs Embedding Performance Test");
        System.out.println("=".repeat(80));

        // Sicherstellen dass Testdaten vorhanden sind
        if (aerzte.isEmpty() || patienten.isEmpty()) {
            throw new IllegalStateException("Testdaten nicht initialisiert! @BeforeAll wurde nicht ausgeführt.");
        }
    }

    @AfterAll
    static void printResults() {
        System.out.println("\n\n");
        System.out.println("╔" + "═".repeat(78) + "╗");
        System.out.println("║" + " ".repeat(20) + "REFERENCING vs EMBEDDING RESULTS" + " ".repeat(24) + "║");
        System.out.println("╠" + "═".repeat(78) + "╣");
        System.out.println("║ Test: " + TEST_DATA_SIZE + " Behandlungen" + " ".repeat(54) + "║");
        System.out.println("╠" + "═".repeat(50) + "╤" + "═".repeat(27) + "╣");
        System.out.println("║ " + padRight("Operation", 48) + " │ " + padRight("Zeit (ms)", 25) + " ║");
        System.out.println("╠" + "═".repeat(50) + "╪" + "═".repeat(27) + "╣");

        for (java.util.Map.Entry<String, Double> entry : performanceResults.entrySet()) {
            String operation = entry.getKey();
            double ms = entry.getValue();
            String timeStr = String.format("%.2f ms", ms);

            System.out.println("║ " + padRight(operation, 48) + " │ " + padRight(timeStr, 25) + " ║");
        }

        System.out.println("╚" + "═".repeat(50) + "╧" + "═".repeat(27) + "╝");

        // Vergleich und Empfehlungen
        printComparison();
    }

    private static void printComparison() {
        System.out.println("\n╔" + "═".repeat(78) + "╗");
        System.out.println("║" + " ".repeat(30) + "ANALYSE & FAZIT" + " ".repeat(33) + "║");
        System.out.println("╠" + "═".repeat(78) + "╣");
        System.out.println("║                                                                              ║");
        System.out.println("║  📊 EMBEDDING (Behandlung mit embedded Arzt/Patient):                        ║");
        System.out.println("║     ✅ Vorteile:                                                             ║");
        System.out.println("║        • Sehr schnelle Reads (alles in einem Dokument)                      ║");
        System.out.println("║        • Keine zusätzlichen Queries/JOINs nötig                             ║");
        System.out.println("║        • Optimal für Read-Heavy Workloads                                   ║");
        System.out.println("║        • Denormalisierung = Performance                                     ║");
        System.out.println("║                                                                              ║");
        System.out.println("║     ❌ Nachteile:                                                            ║");
        System.out.println("║        • Redundanz: Arzt/Patient-Daten mehrfach gespeichert                ║");
        System.out.println("║        • Updates kompliziert: Alle Behandlungen müssen aktualisiert werden  ║");
        System.out.println("║        • Größere Dokumente = mehr Speicher                                  ║");
        System.out.println("║                                                                              ║");
        System.out.println("║  🔗 REFERENCING (BehandlungReferenced mit @DBRef):                          ║");
        System.out.println("║     ✅ Vorteile:                                                             ║");
        System.out.println("║        • Normalisierung: Arzt/Patient nur einmal gespeichert               ║");
        System.out.println("║        • Updates einfach: Änderung an einer Stelle                          ║");
        System.out.println("║        • Konsistenz garantiert                                              ║");
        System.out.println("║        • Kleinere Dokumente                                                 ║");
        System.out.println("║                                                                              ║");
        System.out.println("║     ❌ Nachteile:                                                            ║");
        System.out.println("║        • Langsamere Reads (zusätzliche Queries = JOIN-ähnlich)              ║");
        System.out.println("║        • Netzwerk-Overhead                                                  ║");
        System.out.println("║        • Komplexere Queries                                                 ║");
        System.out.println("║                                                                              ║");
        System.out.println("║  🎯 EMPFEHLUNG:                                                              ║");
        System.out.println("║                                                                              ║");
        System.out.println("║     USE EMBEDDING WHEN:                                                      ║");
        System.out.println("║     • Daten zusammen gelesen werden (1:1 oder 1:N Beziehung)                ║");
        System.out.println("║     • Read-Performance kritisch ist                                         ║");
        System.out.println("║     • Embedded Daten sich selten ändern                                     ║");
        System.out.println("║     • Beispiel: Adresse, Telefon, Medikamente in Behandlung                ║");
        System.out.println("║                                                                              ║");
        System.out.println("║     USE REFERENCING WHEN:                                                    ║");
        System.out.println("║     • Daten häufig aktualisiert werden                                      ║");
        System.out.println("║     • Konsistenz wichtiger als Performance                                  ║");
        System.out.println("║     • Große Dokumente vermieden werden sollen                               ║");
        System.out.println("║     • M:N Beziehungen                                                        ║");
        System.out.println("║                                                                              ║");
        System.out.println("╚" + "═".repeat(78) + "╝");
        System.out.println();
    }

    private static String padRight(String s, int n) {
        if (s.length() >= n) {
            return s.substring(0, n);
        }
        return s + " ".repeat(n - s.length());
    }

    private static void addResult(String operation, double milliseconds) {
        performanceResults.put(operation, milliseconds);
    }


    // ==================== WRITE PERFORMANCE ====================

    @Test
    @Order(1)
    @DisplayName("1. WRITE: Embedding (vollständige Objekte)")
    void testWrite_Embedding() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("WRITE PERFORMANCE - EMBEDDING");
        System.out.println("=".repeat(80));

        behandlungRepository.deleteAll();

        System.out.printf("Erstelle %d Behandlungen mit embedded Arzt/Patient...%n", TEST_DATA_SIZE);

        long startTime = System.nanoTime();

        List<Behandlung> behandlungen = new ArrayList<>();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            Behandlung behandlung = Behandlung.builder()
                .arzt(aerzte.get(random.nextInt(aerzte.size())))
                .patient(patienten.get(random.nextInt(patienten.size())))
                .diagnose("Diagnose-" + i)
                .medikamente(generateMedikamente())
                .beginn(LocalDateTime.now().minusDays(random.nextInt(365)))
                .ende(LocalDateTime.now().minusDays(random.nextInt(365)).plusHours(2))
                .build();
            behandlungen.add(behandlung);
        }

        behandlungRepository.saveAll(behandlungen);

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen erstellt in %.2f ms%n", TEST_DATA_SIZE, duration);
        System.out.printf("  Durchschnitt: %.4f ms/Dokument%n", duration / TEST_DATA_SIZE);
        System.out.printf("  Throughput: %.2f ops/s%n", (TEST_DATA_SIZE / duration) * 1000);

        addResult("WRITE Embedding", duration);
    }

    @Test
    @Order(2)
    @DisplayName("2. WRITE: Referencing (@DBRef)")
    void testWrite_Referencing() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("WRITE PERFORMANCE - REFERENCING (@DBRef)");
        System.out.println("=".repeat(80));

        behandlungReferencedRepository.deleteAll();

        System.out.printf("Erstelle %d Behandlungen mit @DBRef...%n", TEST_DATA_SIZE);

        long startTime = System.nanoTime();

        List<BehandlungReferenced> behandlungen = new ArrayList<>();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            BehandlungReferenced behandlung = BehandlungReferenced.builder()
                .arzt(aerzte.get(random.nextInt(aerzte.size()))) // Nur ObjectId wird gespeichert
                .patient(patienten.get(random.nextInt(patienten.size())))
                .diagnose("Diagnose-" + i)
                .medikamente(generateMedikamente())
                .beginn(LocalDateTime.now().minusDays(random.nextInt(365)))
                .ende(LocalDateTime.now().minusDays(random.nextInt(365)).plusHours(2))
                .build();
            behandlungen.add(behandlung);
        }

        behandlungReferencedRepository.saveAll(behandlungen);

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen erstellt in %.2f ms%n", TEST_DATA_SIZE, duration);
        System.out.printf("  Durchschnitt: %.4f ms/Dokument%n", duration / TEST_DATA_SIZE);
        System.out.printf("  Throughput: %.2f ops/s%n", (TEST_DATA_SIZE / duration) * 1000);

        addResult("WRITE Referencing", duration);

        // Vergleich
        double embeddingTime = performanceResults.get("WRITE Embedding");
        double speedup = embeddingTime / duration;
        System.out.printf("\n📊 Vergleich: Referencing ist %.2fx %s als Embedding%n",
            Math.abs(speedup),
            speedup > 1 ? "schneller" : "langsamer");
    }

    // ==================== READ PERFORMANCE ====================

    @Test
    @Order(3)
    @DisplayName("3. READ: Embedding - Alle Behandlungen")
    void testRead_Embedding() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("READ PERFORMANCE - EMBEDDING");
        System.out.println("=".repeat(80));

        System.out.println("Lese alle Behandlungen (embedded)...");

        long startTime = System.nanoTime();
        List<Behandlung> behandlungen = behandlungRepository.findAll();
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen gelesen in %.2f ms%n", behandlungen.size(), duration);
        System.out.printf("  Durchschnitt: %.4f ms/Dokument%n", duration / behandlungen.size());

        // Zeige ein Beispiel
        if (!behandlungen.isEmpty()) {
            Behandlung first = behandlungen.get(0);
            System.out.println("\n  Beispiel-Dokument:");
            System.out.printf("    Arzt: %s (embedded)%n", first.getArzt() != null ? first.getArzt().getName() : "null");
            System.out.printf("    Patient: %s (embedded)%n", first.getPatient() != null ? first.getPatient().getName() : "null");
        }

        addResult("READ Embedding (findAll)", duration);
    }

    @Test
    @Order(4)
    @DisplayName("4. READ: Referencing - Alle Behandlungen")
    void testRead_Referencing() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("READ PERFORMANCE - REFERENCING (@DBRef)");
        System.out.println("=".repeat(80));

        System.out.println("Lese alle Behandlungen (referenced)...");
        System.out.println("⚠️  Achtung: Jede Behandlung erfordert zusätzliche Queries für Arzt/Patient!");

        long startTime = System.nanoTime();
        List<BehandlungReferenced> behandlungen = behandlungReferencedRepository.findAll();
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen gelesen in %.2f ms%n", behandlungen.size(), duration);
        System.out.printf("  Durchschnitt: %.4f ms/Dokument%n", duration / behandlungen.size());

        // Zeige ein Beispiel
        if (!behandlungen.isEmpty()) {
            BehandlungReferenced first = behandlungen.get(0);
            System.out.println("\n  Beispiel-Dokument:");
            System.out.printf("    Arzt: %s (via @DBRef)%n", first.getArzt() != null ? first.getArzt().getName() : "null");
            System.out.printf("    Patient: %s (via @DBRef)%n", first.getPatient() != null ? first.getPatient().getName() : "null");
        }

        addResult("READ Referencing (findAll)", duration);

        // Vergleich
        double embeddingTime = performanceResults.get("READ Embedding (findAll)");
        double speedup = embeddingTime / duration;
        double overhead = ((duration - embeddingTime) / embeddingTime) * 100;

        System.out.printf("\n📊 Vergleich: Embedding ist %.2fx schneller als Referencing%n", 1 / speedup);
        System.out.printf("   Overhead durch @DBRef: +%.2f%%%n", overhead);
    }

    // ==================== READ WITH FILTER ====================

    @Test
    @Order(5)
    @DisplayName("5. READ: Embedding mit Filter")
    void testReadFiltered_Embedding() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("READ WITH FILTER - EMBEDDING");
        System.out.println("=".repeat(80));

        LocalDateTime cutoff = LocalDateTime.now().minusDays(180);
        System.out.printf("Filtere Behandlungen nach Datum > %s...%n", cutoff.toLocalDate());

        long startTime = System.nanoTime();
        Query query = new Query(Criteria.where("beginn").gte(cutoff));
        List<Behandlung> behandlungen = mongoTemplate.find(query, Behandlung.class);
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen gefunden in %.2f ms%n", behandlungen.size(), duration);

        addResult("READ Filter Embedding", duration);
    }

    @Test
    @Order(6)
    @DisplayName("6. READ: Referencing mit Filter")
    void testReadFiltered_Referencing() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("READ WITH FILTER - REFERENCING");
        System.out.println("=".repeat(80));

        LocalDateTime cutoff = LocalDateTime.now().minusDays(180);
        System.out.printf("Filtere Behandlungen nach Datum > %s...%n", cutoff.toLocalDate());

        long startTime = System.nanoTime();
        Query query = new Query(Criteria.where("beginn").gte(cutoff));
        List<BehandlungReferenced> behandlungen = mongoTemplate.find(query, BehandlungReferenced.class);
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen gefunden in %.2f ms%n", behandlungen.size(), duration);

        addResult("READ Filter Referencing", duration);

        // Vergleich
        double embeddingTime = performanceResults.get("READ Filter Embedding");
        double speedup = embeddingTime / duration;
        System.out.printf("\n📊 Vergleich: Embedding ist %.2fx %s%n",
            Math.abs(1 / speedup),
            speedup < 1 ? "schneller" : "langsamer");
    }

    // ==================== UPDATE PERFORMANCE ====================

    @Test
    @Order(7)
    @DisplayName("7. UPDATE: Arzt-Name ändern - Auswirkung auf Embedding")
    void testUpdate_EmbeddingConsistency() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UPDATE CONSISTENCY - EMBEDDING");
        System.out.println("=".repeat(80));

        Arzt testArzt = aerzte.get(0);
        String oldName = testArzt.getName();
        String newName = "Dr. Updated Name";

        System.out.printf("Ändere Arzt-Name: '%s' → '%s'%n", oldName, newName);
        System.out.println("⚠️  Bei EMBEDDING: Alle Behandlungen mit diesem Arzt müssen aktualisiert werden!");

        // Finde alle Behandlungen mit diesem Arzt
        Query findQuery = new Query(Criteria.where("arzt._id").is(testArzt.getId()));
        List<Behandlung> behandlungenMitArzt = mongoTemplate.find(findQuery, Behandlung.class);

        System.out.printf("   Gefunden: %d Behandlungen mit diesem Arzt%n", behandlungenMitArzt.size());

        // Update in Arzt-Collection
        testArzt.setName(newName);
        arztRepository.save(testArzt);

        // Update in allen Behandlungen (notwendig bei Embedding!)
        long startTime = System.nanoTime();

        for (Behandlung behandlung : behandlungenMitArzt) {
            behandlung.getArzt().setName(newName);
            behandlungRepository.save(behandlung);
        }

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ %d Behandlungen aktualisiert in %.2f ms%n", behandlungenMitArzt.size(), duration);
        System.out.println("   → VIEL AUFWAND bei Embedding!");

        addResult("UPDATE Embedding (cascade)", duration);

        // Zurücksetzen
        testArzt.setName(oldName);
        arztRepository.save(testArzt);
    }

    @Test
    @Order(8)
    @DisplayName("8. UPDATE: Arzt-Name ändern - Auswirkung auf Referencing")
    void testUpdate_ReferencingConsistency() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UPDATE CONSISTENCY - REFERENCING");
        System.out.println("=".repeat(80));

        Arzt testArzt = aerzte.get(0);
        String oldName = testArzt.getName();
        String newName = "Dr. Updated Name";

        System.out.printf("Ändere Arzt-Name: '%s' → '%s'%n", oldName, newName);
        System.out.println("✅ Bei REFERENCING: Nur ein Update nötig!");

        long startTime = System.nanoTime();

        // Nur ein Update in Arzt-Collection
        testArzt.setName(newName);
        arztRepository.save(testArzt);

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.printf("✓ Arzt aktualisiert in %.2f ms%n", duration);
        System.out.println("   → Alle Behandlungen zeigen automatisch den neuen Namen!");

        addResult("UPDATE Referencing (single)", duration);

        // Vergleich
        double embeddingTime = performanceResults.get("UPDATE Embedding (cascade)");
        double speedup = embeddingTime / duration;
        System.out.printf("\n📊 Vergleich: Referencing ist %.2fx schneller bei Updates!%n", speedup);

        // Zurücksetzen
        testArzt.setName(oldName);
        arztRepository.save(testArzt);
    }

    // ==================== HELPER METHODS ====================

    private List<Medikament> generateMedikamente() {
        int count = 1 + random.nextInt(3);
        List<Medikament> medikamente = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            medikamente.add(new Medikament("Medikament-" + i, "Wirkstoff-" + i));
        }
        return medikamente;
    }

    @AfterEach
    void tearDown() {
        // Daten bleiben für nächste Tests
    }
}

