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

    private static final int[] SCALES = {100, 1000, 10000};
    
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
}
