package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
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

class BehandlungsraumRepositoryTest {
    @Autowired
    private BehandlungsraumRepository repository;
    @Autowired
    private ArztRepository arztRepository;
    @Autowired
    private PatientRepository patientRepository;

    private List<Behandlung> behandlungen;
    private Behandlungsraum raum;

    @BeforeEach
    void setUp(){
        var arzt = Arzt.builder()
                .name("Dr. Müller")
                .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .svnr(1234567890L)
                .email(new Email("max@muster.at"))
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();

        var patient = Patient.builder()
                .name("Guid Goatstaller")
                .svnr(213556331L)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .versicherungsart(Versicherungsart.PRIVAT)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();

        behandlungen = List.of(
                Behandlung.builder()
                        .arzt(arzt)
                        .patient(patient)
                        .beginn(LocalDateTime.of(2024,12,3,12,0))
                        .ende(LocalDateTime.of(2024,12,3,13,0))
                        .diagnose("Grippe")
                        .apiKey(ApiKeyGenerator.generateApiKey())
                        .build()
        );

        raum = Behandlungsraum.builder()
                .name("Raum 1")
                .isFrei(true)
                .behandlungen(behandlungen)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
    }

    @Test
    void can_save(){
        assertThat(repository.saveAndFlush(raum).getBehandlungsraumId()).isNotNull();
    }

    @Test
    void can_findByName(){
        repository.saveAndFlush(raum);
        assertThat(repository.findByNameContainingIgnoreCase("Raum 1")).isNotEmpty();
    }

    @Test
    void can_findAllMinimal(){
        repository.saveAndFlush(raum);
        var result = repository.findAllMinimalBy();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).name()).isEqualTo("Raum 1");
    }

    @Test
    void can_findAllProjectedBy(){
        repository.saveAndFlush(raum);
        var result = repository.findAllProjectedBy();
        assertThat(result).isNotEmpty();
        //assertThat(result.get(0).behandlungen()).isNotEmpty();
    }
}