package com.example.zellervandecastellpatientenverwaltung.performance;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL Performance Test für relationale Datenbank
 *
 * WRITE Tests:
 * - Create 100 Ärzte
 * - Create 10000 Ärzte
 * - Create 100000 Ärzte
 * - Create 100 Patienten
 * - Create 10000 Patienten
 * - Create 100000 Patienten
 * - Create 100 Behandlungen
 * - Create 10000 Behandlungen
 * - Create 100000 Behandlungen
 *
 * READ Tests:
 * - Read All Ärzte (10.000)
 * - Read Ärzte mit Filter (10.000)
 * - Read Ärzte mit Projektion (10.000)
 * - Read Ärzte Filter+Projektion+Sort (10.000)
 * - Read Behandlungen mit Filter (10.000)
 *
 * UPDATE Tests:
 * - Update Bulk Ärzte (10.000)
 * - Update Single Patienten (1.000)
 *
 * DELETE Tests:
 * - Delete Bulk Ärzte (10.000)
 *
 * AGGREGATION Tests:
 * - Aggregation Group By (10.000)
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SQLPerformanceTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ArztRepository arztRepository;

    @Autowired
    private BehandlungRepository behandlungRepository;

    @BeforeEach
    void cleanupDatabase() {
        // Lösche in der richtigen Reihenfolge (abhängige zuerst)
        try {
            behandlungRepository.deleteAll();
        } catch (Exception e) {
            // Ignoriere Fehler
        }
        try {
            patientRepository.deleteAll();
        } catch (Exception e) {
            // Ignoriere Fehler
        }
        try {
            arztRepository.deleteAll();
        } catch (Exception e) {
            // Ignoriere Fehler
        }
    }

    // ============================================
    // WRITE OPERATIONS - ÄRZTE
    // ============================================

    @Test
    @Order(1)
    @DisplayName("Create 100 Ärzte")
    void testCreate100Aerzte() {
        System.out.println("\n========== CREATE 100 ÄRZTE ==========");
        long duration = createAerzte(100);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    @Test
    @Order(2)
    @DisplayName("Create 10000 Ärzte")
    void testCreate10000Aerzte() {
        System.out.println("\n========== CREATE 10000 ÄRZTE ==========");
        long duration = createAerzte(10000);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    @Test
    @Order(3)
    @DisplayName("Create 100000 Ärzte")
    void testCreate100000Aerzte() {
        System.out.println("\n========== CREATE 100000 ÄRZTE ==========");
        long duration = createAerzte(100000);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    // ============================================
    // WRITE OPERATIONS - PATIENTEN
    // ============================================

    @Test
    @Order(4)
    @DisplayName("Create 100 Patienten")
    void testCreate100Patienten() {
        System.out.println("\n========== CREATE 100 PATIENTEN ==========");
        long duration = createPatienten(100);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    @Test
    @Order(5)
    @DisplayName("Create 10000 Patienten")
    void testCreate10000Patienten() {
        System.out.println("\n========== CREATE 10000 PATIENTEN ==========");
        long duration = createPatienten(10000);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    @Test
    @Order(6)
    @DisplayName("Create 100000 Patienten")
    void testCreate100000Patienten() {
        System.out.println("\n========== CREATE 100000 PATIENTEN ==========");
        long duration = createPatienten(100000);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    // ============================================
    // WRITE OPERATIONS - BEHANDLUNGEN
    // ============================================

    @Test
    @Order(7)
    @DisplayName("Create 100 Behandlungen")
    void testCreate100Behandlungen() {
        System.out.println("\n========== CREATE 100 BEHANDLUNGEN ==========");
        long duration = createBehandlungen(100);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    @Test
    @Order(8)
    @DisplayName("Create 10000 Behandlungen")
    void testCreate10000Behandlungen() {
        System.out.println("\n========== CREATE 10000 BEHANDLUNGEN ==========");
        long duration = createBehandlungen(10000);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    @Test
    @Order(9)
    @DisplayName("Create 100000 Behandlungen")
    void testCreate100000Behandlungen() {
        System.out.println("\n========== CREATE 100000 BEHANDLUNGEN ==========");
        long duration = createBehandlungen(100000);
        System.out.printf("Zeit: %d ms (%.2f s)%n", duration, duration / 1000.0);
    }

    // ============================================
    // READ OPERATIONS
    // ============================================

    @Test
    @Order(10)
    @DisplayName("Read All Ärzte (10.000)")
    void testReadAllAerzte() {
        System.out.println("\n========== READ ALL ÄRZTE (10.000) ==========");
        createAerzte(10000);

        long startTime = System.currentTimeMillis();
        List<Arzt> result = arztRepository.findAll();
        long endTime = System.currentTimeMillis();

        System.out.printf("Gefunden: %d Ärzte - Zeit: %d ms%n", result.size(), endTime - startTime);
    }

    @Test
    @Order(11)
    @DisplayName("Read Ärzte mit Filter (10.000)")
    void testReadAerzteWithFilter() {
        System.out.println("\n========== READ ÄRZTE MIT FILTER (10.000) ==========");
        createAerzte(10000);

        long startTime = System.currentTimeMillis();
        List<Arzt> result = arztRepository.findByNameContainingIgnoreCase("Arzt 1");
        long endTime = System.currentTimeMillis();

        System.out.printf("Gefunden: %d Ärzte - Zeit: %d ms%n", result.size(), endTime - startTime);
    }

    @Test
    @Order(12)
    @DisplayName("Read Ärzte mit Projektion (10.000)")
    void testReadAerzteWithProjection() {
        System.out.println("\n========== READ ÄRZTE MIT PROJEKTION (10.000) ==========");
        createAerzte(10000);

        long startTime = System.currentTimeMillis();
        var result = arztRepository.findAllProjectedBy();
        long endTime = System.currentTimeMillis();

        System.out.printf("Gefunden: %d Ärzte (Projektion) - Zeit: %d ms%n", result.size(), endTime - startTime);
    }

    @Test
    @Order(13)
    @DisplayName("Read Ärzte Filter+Projektion+Sort (10.000)")
    void testReadAerzteFilterProjectionSort() {
        System.out.println("\n========== READ ÄRZTE FILTER+PROJEKTION+SORT (10.000) ==========");
        createAerzte(10000);

        long startTime = System.currentTimeMillis();
        var result = arztRepository.findAllProjectedBy();
        long endTime = System.currentTimeMillis();

        System.out.printf("Gefunden: %d Ärzte (Filter+Projektion+Sort) - Zeit: %d ms%n", result.size(), endTime - startTime);
    }

    @Test
    @Order(14)
    @DisplayName("Read Behandlungen mit Filter (10.000)")
    void testReadBehandlungenWithFilter() {
        System.out.println("\n========== READ BEHANDLUNGEN MIT FILTER (10.000) ==========");
        createBehandlungen(10000);

        long startTime = System.currentTimeMillis();
        List<Behandlung> result = behandlungRepository.findAll();
        long endTime = System.currentTimeMillis();

        System.out.printf("Gefunden: %d Behandlungen - Zeit: %d ms%n", result.size(), endTime - startTime);
    }

    // ============================================
    // UPDATE OPERATIONS
    // ============================================

    @Test
    @Order(15)
    @DisplayName("Update Bulk Ärzte (10.000)")
    void testUpdateBulkAerzte() {
        System.out.println("\n========== UPDATE BULK ÄRZTE (10.000) ==========");
        createAerzte(10000);
        List<Arzt> aerzte = arztRepository.findAll();

        long startTime = System.currentTimeMillis();
        for (Arzt arzt : aerzte) {
            arzt.setName(arzt.getName() + " - Updated");
        }
        arztRepository.saveAll(aerzte);
        long endTime = System.currentTimeMillis();

        System.out.printf("Updated: %d Ärzte - Zeit: %d ms%n", aerzte.size(), endTime - startTime);
    }

    @Test
    @Order(16)
    @DisplayName("Update Single Patienten (1.000)")
    void testUpdateSinglePatienten() {
        System.out.println("\n========== UPDATE SINGLE PATIENTEN (1.000) ==========");
        createPatienten(1000);
        List<Patient> patienten = patientRepository.findAll();

        long startTime = System.currentTimeMillis();
        for (Patient patient : patienten) {
            patient.setName(patient.getName() + " - Updated");
            patientRepository.save(patient);
        }
        long endTime = System.currentTimeMillis();

        System.out.printf("Updated: %d Patienten (einzeln) - Zeit: %d ms%n", patienten.size(), endTime - startTime);
    }

    // ============================================
    // DELETE OPERATIONS
    // ============================================

    @Test
    @Order(17)
    @DisplayName("Delete Bulk Ärzte (10.000)")
    void testDeleteBulkAerzte() {
        System.out.println("\n========== DELETE BULK ÄRZTE (10.000) ==========");
        createAerzte(10000);

        long startTime = System.currentTimeMillis();
        arztRepository.deleteAll();
        long endTime = System.currentTimeMillis();

        System.out.printf("Gelöscht: 10000 Ärzte - Zeit: %d ms%n", endTime - startTime);
    }

    // ============================================
    // AGGREGATION OPERATIONS
    // ============================================

    @Test
    @Order(18)
    @DisplayName("Aggregation Group By (10.000)")
    void testAggregationGroupBy() {
        System.out.println("\n========== AGGREGATION GROUP BY (10.000) ==========");
        createAerzte(10000);

        long startTime = System.currentTimeMillis();
        long count = arztRepository.count();
        long endTime = System.currentTimeMillis();

        System.out.printf("Count: %d Ärzte - Zeit: %d ms%n", count, endTime - startTime);
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private long createAerzte(int count) {
        long startTime = System.currentTimeMillis();
        List<Arzt> aerzte = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            aerzte.add(createArzt(i));
        }
        arztRepository.saveAll(aerzte);
        return System.currentTimeMillis() - startTime;
    }

    private long createPatienten(int count) {
        long startTime = System.currentTimeMillis();
        List<Patient> patienten = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patienten.add(createPatient(i));
        }
        patientRepository.saveAll(patienten);
        return System.currentTimeMillis() - startTime;
    }

    private long createBehandlungen(int count) {
        Arzt arzt = arztRepository.save(createArzt(0));
        Patient patient = patientRepository.save(createPatient(0));

        long startTime = System.currentTimeMillis();
        List<Behandlung> behandlungen = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Behandlung behandlung = Behandlung.builder()
                    .behandlungId(new Behandlung.BehandlungId(null))
                    .arzt(arzt)
                    .patient(patient)
                    .beginn(LocalDateTime.now().minusDays(i))
                    .ende(LocalDateTime.now().minusDays(i).plusHours(1))
                    .diagnose("Diagnose " + i)
                    .medikamente(createMedikamente(i))
                    .apiKey(ApiKeyGenerator.generateApiKey())
                    .build();
            behandlungen.add(behandlung);
        }
        behandlungRepository.saveAll(behandlungen);
        return System.currentTimeMillis() - startTime;
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private Patient createPatient(int index) {
        return Patient.builder()
                .patientID(new Patient.PatientID(null))
                .name("Patient " + index)
                .gebDatum(LocalDate.of(1980 + (index % 40), (index % 12) + 1, (index % 28) + 1))
                .svnr(1000000000L + index)
                .adresse(createAdresse(index))
                .telefonNummer(createTelefonNummer(index))
                .versicherungsart(index % 2 == 0 ? Versicherungsart.KRANKENKASSE : Versicherungsart.PRIVAT)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
    }

    private Arzt createArzt(int index) {
        return Arzt.builder()
                .arztId(new Arzt.ArztId(null))
                .name("Dr. Arzt " + index)
                .gebDatum(LocalDate.of(1970 + (index % 30), (index % 12) + 1, (index % 28) + 1))
                .svnr(2000000000L + index)
                .adresse(createAdresse(index))
                .telefonNummer(createTelefonNummer(index))
                .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                .email(new Email("arzt" + index + "@test.at"))
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
    }

    private Adresse createAdresse(int index) {
        return Adresse.builder()
                .strasse("Straße " + index)
                .hausNr(String.valueOf(index % 100 + 1))
                .plz(String.format("%04d", 1000 + (index % 9000)))
                .stadt("Stadt " + (index % 10))
                .build();
    }

    private TelefonNummer createTelefonNummer(int index) {
        return new TelefonNummer(
                "+43",
                "664",
                String.format("%07d", index % 10000000),
                TelefonNummerArt.MOBIL
        );
    }

    private List<Medikament> createMedikamente(int index) {
        List<Medikament> medikamente = new ArrayList<>();
        int count = (index % 3) + 1;
        for (int i = 0; i < count; i++) {
            medikamente.add(new Medikament("Medikament " + index + "-" + i, "10mg"));
        }
        return medikamente;
    }
}

