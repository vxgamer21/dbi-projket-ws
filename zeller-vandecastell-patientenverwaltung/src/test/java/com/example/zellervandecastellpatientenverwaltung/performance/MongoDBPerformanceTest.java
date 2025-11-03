package com.example.zellervandecastellpatientenverwaltung.performance;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB CRUD Performance Tests
 * Tests Arzt, Patient, Behandlung, Medikament entities at different scales
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoDBPerformanceTest {

    @Autowired
    private ArztRepository arztRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BehandlungRepository behandlungRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final int[] SCALES = {100, 1000, 100000};
    
    private List<String> arztIds;
    private List<String> patientIds;

    @BeforeEach
    void setUp() {
        // Clean all collections before each test
        arztRepository.deleteAll();
        patientRepository.deleteAll();
        behandlungRepository.deleteAll();
        
        arztIds = new ArrayList<>();
        patientIds = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        arztRepository.deleteAll();
        patientRepository.deleteAll();
        behandlungRepository.deleteAll();
    }

    /**
     * Test 1: Write Performance - Insert Arzt entities
     */
    @Test
    @Order(1)
    @DisplayName("Write Performance Test - Arzt Inserts")
    void testWritePerformanceArzt() {
        System.out.println("\n=== WRITE PERFORMANCE TEST - ARZT ===");
        
        for (int scale : SCALES) {
            List<Arzt> aerzte = new ArrayList<>();
            
            // Prepare data
            for (int i = 0; i < scale; i++) {
                Arzt arzt = Arzt.builder()
                        .name("Dr. Arzt " + i)
                        .gebDatum(LocalDate.of(1970, 1, 1).plusDays(i % 10000))
                        .svnr(1000000L + i)
                        .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                        .email(new Email("arzt" + i + "@test.com"))
                        .apiKey("key-" + i)
                        .build();
                aerzte.add(arzt);
            }
            
            // Measure insert time
            long startTime = System.currentTimeMillis();
            arztRepository.saveAll(aerzte);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Inserted %d Arzt records in %d ms (%.2f records/sec)%n",
                    scale, scale, duration, (scale * 1000.0 / duration));
            
            arztRepository.deleteAll();
        }
    }

    /**
     * Test 2: Write Performance - Insert Patient entities
     */
    @Test
    @Order(2)
    @DisplayName("Write Performance Test - Patient Inserts")
    void testWritePerformancePatient() {
        System.out.println("\n=== WRITE PERFORMANCE TEST - PATIENT ===");
        
        for (int scale : SCALES) {
            List<Patient> patienten = new ArrayList<>();
            
            // Prepare data
            for (int i = 0; i < scale; i++) {
                Patient patient = Patient.builder()
                        .name("Patient " + i)
                        .gebDatum(LocalDate.of(1980, 1, 1).plusDays(i % 10000))
                        .svnr(2000000L + i)
                        .versicherungsart(i % 2 == 0 ? Versicherungsart.PRIVAT : Versicherungsart.KRANKENKASSE)
                        .adresse(new Adresse("Straße " + i, "1" + (i % 10), "1010", "Wien"))
                        .apiKey("patient-key-" + i)
                        .build();
                patienten.add(patient);
            }
            
            // Measure insert time
            long startTime = System.currentTimeMillis();
            patientRepository.saveAll(patienten);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Inserted %d Patient records in %d ms (%.2f records/sec)%n",
                    scale, scale, duration, (scale * 1000.0 / duration));
            
            patientRepository.deleteAll();
        }
    }

    /**
     * Test 3: Find Performance - No Filter
     */
    @Test
    @Order(3)
    @DisplayName("Find Performance Test - No Filter")
    void testFindPerformanceNoFilter() {
        System.out.println("\n=== FIND PERFORMANCE TEST - NO FILTER ===");
        
        for (int scale : SCALES) {
            // Setup: Insert test data
            setupTestData(scale);
            
            // Measure findAll time
            long startTime = System.currentTimeMillis();
            List<Arzt> aerzte = arztRepository.findAll();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Found %d Arzt records in %d ms (%.2f records/sec)%n",
                    scale, aerzte.size(), duration, (aerzte.size() * 1000.0 / duration));
            
            // Cleanup
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    /**
     * Test 4: Find Performance - With Filter
     */
    @Test
    @Order(4)
    @DisplayName("Find Performance Test - With Filter")
    void testFindPerformanceWithFilter() {
        System.out.println("\n=== FIND PERFORMANCE TEST - WITH FILTER ===");
        
        for (int scale : SCALES) {
            // Setup: Insert test data
            setupTestData(scale);
            
            // Measure find with filter time (search by Fachgebiet)
            Query query = new Query(Criteria.where("fachgebiet").is(Fachgebiet.ALLGEMEINMEDIZIN.name()));
            
            long startTime = System.currentTimeMillis();
            List<Arzt> aerzte = mongoTemplate.find(query, Arzt.class);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Found %d Arzt records with filter in %d ms (%.2f records/sec)%n",
                    scale, aerzte.size(), duration, (aerzte.size() * 1000.0 / duration));
            
            // Cleanup
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    /**
     * Test 5: Find Performance - With Filter and Projection
     */
    @Test
    @Order(5)
    @DisplayName("Find Performance Test - With Filter and Projection")
    void testFindPerformanceWithFilterAndProjection() {
        System.out.println("\n=== FIND PERFORMANCE TEST - WITH FILTER AND PROJECTION ===");
        
        for (int scale : SCALES) {
            // Setup: Insert test data
            setupTestData(scale);
            
            // Measure find with filter and projection (only name and fachgebiet)
            Query query = new Query(Criteria.where("fachgebiet").is(Fachgebiet.ALLGEMEINMEDIZIN.name()));
            query.fields().include("name").include("fachgebiet");
            
            long startTime = System.currentTimeMillis();
            List<Arzt> aerzte = mongoTemplate.find(query, Arzt.class);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Found %d Arzt records with filter+projection in %d ms (%.2f records/sec)%n",
                    scale, aerzte.size(), duration, (aerzte.size() * 1000.0 / duration));
            
            // Cleanup
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    /**
     * Test 6: Find Performance - With Filter, Projection and Sorting
     */
    @Test
    @Order(6)
    @DisplayName("Find Performance Test - With Filter, Projection and Sorting")
    void testFindPerformanceWithFilterProjectionAndSort() {
        System.out.println("\n=== FIND PERFORMANCE TEST - WITH FILTER, PROJECTION AND SORTING ===");
        
        for (int scale : SCALES) {
            // Setup: Insert test data
            setupTestData(scale);
            
            // Measure find with filter, projection and sorting
            Query query = new Query(Criteria.where("fachgebiet").is(Fachgebiet.ALLGEMEINMEDIZIN.name()));
            query.fields().include("name").include("fachgebiet");
            query.with(Sort.by(Sort.Direction.ASC, "name"));
            
            long startTime = System.currentTimeMillis();
            List<Arzt> aerzte = mongoTemplate.find(query, Arzt.class);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Found %d Arzt records with filter+projection+sort in %d ms (%.2f records/sec)%n",
                    scale, aerzte.size(), duration, (aerzte.size() * 1000.0 / duration));
            
            // Cleanup
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    /**
     * Test 7: Update Performance
     */
    @Test
    @Order(7)
    @DisplayName("Update Performance Test")
    void testUpdatePerformance() {
        System.out.println("\n=== UPDATE PERFORMANCE TEST ===");
        
        for (int scale : SCALES) {
            // Setup: Insert test data
            setupTestData(scale);
            
            // Get all patients to update
            List<Patient> patienten = patientRepository.findAll();
            
            // Update all patients' Versicherungsart
            for (Patient patient : patienten) {
                patient.setVersicherungsart(Versicherungsart.PRIVAT);
            }
            
            // Measure update time
            long startTime = System.currentTimeMillis();
            patientRepository.saveAll(patienten);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Updated %d Patient records in %d ms (%.2f records/sec)%n",
                    scale, patienten.size(), duration, (patienten.size() * 1000.0 / duration));
            
            // Cleanup
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    /**
     * Test 8: Delete Performance
     */
    @Test
    @Order(8)
    @DisplayName("Delete Performance Test")
    void testDeletePerformance() {
        System.out.println("\n=== DELETE PERFORMANCE TEST ===");
        
        for (int scale : SCALES) {
            // Setup: Insert test data
            setupTestData(scale);
            
            // Measure delete time for Behandlung
            long behandlungCount = behandlungRepository.count();
            
            long startTime = System.currentTimeMillis();
            behandlungRepository.deleteAll();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.printf("Scale %d: Deleted %d Behandlung records in %d ms (%.2f records/sec)%n",
                    scale, behandlungCount, duration, (behandlungCount * 1000.0 / duration));
            
            // Cleanup remaining
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    /**
     * Helper method to setup test data
     */
    private void setupTestData(int scale) {
        arztIds.clear();
        patientIds.clear();
        
        // Insert Aerzte
        List<Arzt> aerzte = new ArrayList<>();
        for (int i = 0; i < scale; i++) {
            Arzt arzt = Arzt.builder()
                    .name("Dr. Test " + i)
                    .gebDatum(LocalDate.of(1970, 1, 1).plusDays(i % 1000))
                    .svnr(1000000L + i)
                    .fachgebiet(i % 3 == 0 ? Fachgebiet.ALLGEMEINMEDIZIN : Fachgebiet.CHIRURGIE)
                    .email(new Email("test" + i + "@test.com"))
                    .apiKey("key-" + i)
                    .build();
            aerzte.add(arzt);
        }
        List<Arzt> savedAerzte = arztRepository.saveAll(aerzte);
        savedAerzte.forEach(a -> arztIds.add(a.getId()));
        
        // Insert Patienten
        List<Patient> patienten = new ArrayList<>();
        for (int i = 0; i < scale; i++) {
            Patient patient = Patient.builder()
                    .name("Test Patient " + i)
                    .gebDatum(LocalDate.of(1980, 1, 1).plusDays(i % 1000))
                    .svnr(2000000L + i)
                    .versicherungsart(i % 2 == 0 ? Versicherungsart.PRIVAT : Versicherungsart.KRANKENKASSE)
                    .apiKey("patient-key-" + i)
                    .build();
            patienten.add(patient);
        }
        List<Patient> savedPatienten = patientRepository.saveAll(patienten);
        savedPatienten.forEach(p -> patientIds.add(p.getId()));
        
        // Insert Behandlungen with Medikamente
        List<Behandlung> behandlungen = new ArrayList<>();
        for (int i = 0; i < Math.min(scale, arztIds.size()); i++) {
            List<Medikament> medikamente = new ArrayList<>();
            medikamente.add(new Medikament("Aspirin", "Acetylsalicylsäure"));
            medikamente.add(new Medikament("Ibuprofen", "Ibuprofen"));
            
            Behandlung behandlung = Behandlung.builder()
                    .arztId(arztIds.get(i % arztIds.size()))
                    .patientId(patientIds.get(i % patientIds.size()))
                    .medikamente(medikamente)
                    .beginn(LocalDateTime.now().minusDays(i % 30))
                    .ende(LocalDateTime.now().minusDays(i % 30).plusHours(2))
                    .diagnose("Diagnose " + i)
                    .apiKey("behandlung-key-" + i)
                    .build();
            behandlungen.add(behandlung);
        }
        behandlungRepository.saveAll(behandlungen);
    }

    @Test
    @Order(2) // ggf. Order anpassen (und die folgenden Orders +1 schieben)
    @DisplayName("Write Performance Test - Behandlung Inserts")
    void testWritePerformanceBehandlung() {
        System.out.println("\n=== WRITE PERFORMANCE TEST - BEHANDLUNG ===");

        for (int scale : SCALES) {
            // Für gültige Referenzen zuerst Ärzte & Patienten anlegen
            setupDoctorsAndPatientsOnly(scale);

            List<Behandlung> behandlungen = buildBehandlungen(scale);

            long startTime = System.currentTimeMillis();
            behandlungRepository.saveAll(behandlungen);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.printf("Scale %d: Inserted %d Behandlung records in %d ms (%.2f records/sec)%n",
                    scale, behandlungen.size(), duration, (behandlungen.size() * 1000.0 / duration));

            // Aufräumen
            behandlungRepository.deleteAll();
            arztRepository.deleteAll();
            patientRepository.deleteAll();
        }
    }

    // --- NEU: Find Performance - Behandlung, ohne Filter ---
    @Test
    @Order(4)
    @DisplayName("Find Performance Test (Behandlung) - No Filter")
    void testFindPerformanceBehandlung_NoFilter() {
        System.out.println("\n=== FIND PERFORMANCE TEST (BEHANDLUNG) - NO FILTER ===");

        for (int scale : SCALES) {
            setupTestData(scale); // legt Ärzte, Patienten, Behandlungen an

            long startTime = System.currentTimeMillis();
            List<Behandlung> list = behandlungRepository.findAll();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.printf("Scale %d: Found %d Behandlung records in %d ms (%.2f records/sec)%n",
                    scale, list.size(), duration, (list.size() * 1000.0 / duration));

            cleanupAll();
        }
    }

    // --- NEU: Find Performance - Behandlung, mit Filter ---
    @Test
    @Order(5)
    @DisplayName("Find Performance Test (Behandlung) - With Filter")
    void testFindPerformanceBehandlung_WithFilter() {
        System.out.println("\n=== FIND PERFORMANCE TEST (BEHANDLUNG) - WITH FILTER ===");

        for (int scale : SCALES) {
            setupTestData(scale);

            // Beispiel-Filter: Diagnose-Präfix + Zeitraum
            Query q = new Query(
                    Criteria.where("diagnose").regex("^Diagnose [0-9]+$")
                            .and("beginn").gte(LocalDateTime.now().minusDays(30))
            );

            long startTime = System.currentTimeMillis();
            List<Behandlung> list = mongoTemplate.find(q, Behandlung.class);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.printf("Scale %d: Found %d Behandlung records with filter in %d ms (%.2f records/sec)%n",
                    scale, list.size(), duration, (list.size() * 1000.0 / duration));

            cleanupAll();
        }
    }

    // --- NEU: Find Performance - Behandlung, Filter + Projection + Sort ---
    @Test
    @Order(6)
    @DisplayName("Find Performance Test (Behandlung) - With Filter, Projection and Sorting")
    void testFindPerformanceBehandlung_WithFilterProjectionSort() {
        System.out.println("\n=== FIND PERFORMANCE TEST (BEHANDLUNG) - WITH FILTER + PROJECTION + SORT ===");

        for (int scale : SCALES) {
            setupTestData(scale);

            Query q = new Query(Criteria.where("arztId").is(arztIds.get(0)));
            q.fields().include("arztId").include("patientId").include("beginn").include("diagnose");
            q.with(Sort.by(Sort.Direction.DESC, "beginn"));

            long startTime = System.currentTimeMillis();
            List<Behandlung> list = mongoTemplate.find(q, Behandlung.class);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.printf("Scale %d: Found %d Behandlung records with filter+projection+sort in %d ms (%.2f records/sec)%n",
                    scale, list.size(), duration, (list.size() * 1000.0 / duration));

            cleanupAll();
        }
    }

    // --- NEU: Update Performance - Behandlung ---
    @Test
    @Order(7)
    @DisplayName("Update Performance Test - Behandlung")
    void testUpdatePerformanceBehandlung() {
        System.out.println("\n=== UPDATE PERFORMANCE TEST - BEHANDLUNG ===");

        for (int scale : SCALES) {
            setupTestData(scale);

            List<Behandlung> list = behandlungRepository.findAll();
            // Beispiel-Update: Diagnose und (falls leer) ein Medikament hinzufügen
            for (int i = 0; i < list.size(); i++) {
                Behandlung b = list.get(i);
                b.setDiagnose("Diagnose geändert " + i);
                if (b.getMedikamente() == null || b.getMedikamente().isEmpty()) {
                    List<Medikament> meds = new ArrayList<>();
                    meds.add(new Medikament("Paracetamol", "Paracetamol"));
                    b.setMedikamente(meds);
                }
            }

            long startTime = System.currentTimeMillis();
            behandlungRepository.saveAll(list);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.printf("Scale %d: Updated %d Behandlung records in %d ms (%.2f records/sec)%n",
                    scale, list.size(), duration, (list.size() * 1000.0 / duration));

            cleanupAll();
        }
    }

    // --- HELFER: nur Ärzte & Patienten anlegen (für reinen Behandlung-Write-Test) ---
    private void setupDoctorsAndPatientsOnly(int scale) {
        arztIds.clear();
        patientIds.clear();

        List<Arzt> aerzte = new ArrayList<>();
        for (int i = 0; i < scale; i++) {
            aerzte.add(Arzt.builder()
                    .name("Dr. Test " + i)
                    .gebDatum(LocalDate.of(1970, 1, 1).plusDays(i % 1000))
                    .svnr(1000000L + i)
                    .fachgebiet(i % 3 == 0 ? Fachgebiet.ALLGEMEINMEDIZIN : Fachgebiet.CHIRURGIE)
                    .email(new Email("test" + i + "@test.com"))
                    .apiKey("key-" + i)
                    .build());
        }
        arztRepository.saveAll(aerzte).forEach(a -> arztIds.add(a.getId()));

        List<Patient> patienten = new ArrayList<>();
        for (int i = 0; i < scale; i++) {
            patienten.add(Patient.builder()
                    .name("Test Patient " + i)
                    .gebDatum(LocalDate.of(1980, 1, 1).plusDays(i % 1000))
                    .svnr(2000000L + i)
                    .versicherungsart(i % 2 == 0 ? Versicherungsart.PRIVAT : Versicherungsart.KRANKENKASSE)
                    .apiKey("patient-key-" + i)
                    .build());
        }
        patientRepository.saveAll(patienten).forEach(p -> patientIds.add(p.getId()));
    }

    // --- HELFER: Behandlungen generieren (0..2 Medikamente) ---
    private List<Behandlung> buildBehandlungen(int scale) {
        List<Behandlung> behandlungen = new ArrayList<>(scale);
        for (int i = 0; i < scale; i++) {
            List<Medikament> medikamente = new ArrayList<>();
            int m = i % 3; // 0,1,2 Medikamente
            if (m >= 1) medikamente.add(new Medikament("Aspirin", "Acetylsalicylsäure"));
            if (m == 2) medikamente.add(new Medikament("Ibuprofen", "Ibuprofen"));

            behandlungen.add(Behandlung.builder()
                    .arztId(arztIds.get(i % arztIds.size()))
                    .patientId(patientIds.get(i % patientIds.size()))
                    .medikamente(medikamente) // kann leer sein
                    .beginn(LocalDateTime.now().minusDays(i % 30))
                    .ende(LocalDateTime.now().minusDays(i % 30).plusHours(2))
                    .diagnose("Diagnose " + i)
                    .apiKey("behandlung-key-" + i)
                    .build());
        }
        return behandlungen;
    }

    // --- HELFER: gemeinsames Aufräumen ---
    private void cleanupAll() {
        behandlungRepository.deleteAll();
        arztRepository.deleteAll();
        patientRepository.deleteAll();
    }
}
