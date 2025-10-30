package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import jakarta.persistence.Embedded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)

class BehandlungRepositoryTest {
    @Autowired
    private BehandlungRepository repository;
    @Autowired
    private ArztRepository arztRepository;
    @Autowired
    private PatientRepository patientRepository;

    private Arzt arzt;
    private Patient patient;
    private LocalDateTime beginn;
    private LocalDateTime ende;
    private Behandlung behandlung;
    @Embedded
    private Medikament medikament;
    private String apiKey1;
    private String apiKey2;
    private String apiKey3;

    @BeforeEach
    void setUp(){
        apiKey1 = ApiKeyGenerator.generateApiKey();
        apiKey2 = ApiKeyGenerator.generateApiKey();
        apiKey3 = ApiKeyGenerator.generateApiKey();

        arzt = Arzt.builder()
                .name("Dr. Müller")
                .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .svnr(1234567890L)
                .apiKey(apiKey1)
                .build();

        beginn = LocalDateTime.of(2024, 12, 3, 12, 0);
        ende = LocalDateTime.of(2024, 12, 3, 13, 0);

        patient = Patient.builder()
                .name("Max Mustermann")
                .gebDatum(LocalDate.of(1990, 1, 1))
                .svnr(123456789L)
                .apiKey(apiKey2)
                .build();

        patient = patientRepository.saveAndFlush(patient);
        arzt = arztRepository.saveAndFlush(arzt);

        medikament = new Medikament("Ibuprofen", "Schmerzlindernt");

        behandlung = Behandlung.builder()
                .patient(patient)
                .arzt(arzt)
                .beginn(beginn)
                .ende(ende)
                .diagnose("Kopfschmerzen")
                .medikamente(List.of(medikament))
                .apiKey(apiKey3)
                .build();
    }

    @Test
    void can_save(){
        Behandlung saved = repository.saveAndFlush(behandlung);
        assertThat(saved.getBehandlungId()).isNotNull();
    }

    @Test
    void can_findbyDiagnose() {
        repository.saveAndFlush(behandlung);
        List<Behandlung> found = repository.findByDiagnoseContainingIgnoreCase("Kopfschmerzen");
        assertThat(found).isNotEmpty();
        assertThat(found.getFirst().getDiagnose()).isEqualTo("Kopfschmerzen");
    }

    @Test
    void can_findAllMinimal() {
        repository.saveAndFlush(behandlung);
        var result = repository.findAllMinimalBy();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).diagnose()).isEqualTo("Kopfschmerzen");
    }

    @Test
    void can_findAllProjectedBy() {
        repository.saveAndFlush(behandlung);
        var result = repository.findAllProjectedBy();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).arzt().name()).isEqualTo("Dr. Müller");
        //assertThat(result.get(0).patient().name()).isEqualTo("Max Mustermann");
    }
}