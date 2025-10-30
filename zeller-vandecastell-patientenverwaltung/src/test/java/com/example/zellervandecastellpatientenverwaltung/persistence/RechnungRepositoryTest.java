package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.dtos.RechnungDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.RechnungDtos;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

class RechnungRepositoryTest {
    @Autowired
    private RechnungRepository repository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ArztRepository arztRepository;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "arzt_id")
    private Arzt arzt;

    private LocalDateTime datum;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private Rechnung rechnung;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        adresse = Adresse.builder()
                .strasse("Musterstraße")
                .hausNr("1")
                .plz("1234")
                .stadt("Musterstadt")
                .build();

        patient = Patient.builder()
                .name("Max Mustermann")
                .gebDatum(LocalDate.of(1990, 1, 1))
                .svnr(123456789L)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();

        arzt = Arzt.builder()
                .name("Dr. Müller")
                .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .svnr(1234567890L)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();

        patient = patientRepository.save(patient);
        arzt = arztRepository.save(arzt);

        rechnung = Rechnung.builder()
                .patient(patient)
                .arzt(arzt)
                .betrag(150.00)
                .bezahlt(false)
                .zahlungsart(Zahlungsart.BARZAHLUNG)
                .datum(LocalDateTime.now())
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
    }


    @Test
    void can_save() {
        Rechnung save;
        save = repository.saveAndFlush(rechnung);
        assertThat(save.getRechnungId()).isNotNull();
    }

    @Test
    void can_findById(){
        Rechnung saved = repository.saveAndFlush(rechnung);
        assertThat(repository.findById(saved.getRechnungId())).isNotEmpty();
        assertThat(repository.findById(saved.getRechnungId()).get().getRechnungId()).isEqualTo(saved.getRechnungId());

    }

    @Test
    void can_findAllProjectedBy() {
        repository.saveAndFlush(rechnung);
        List<RechnungDto> projections = repository.findAllProjectedBy();
        assertThat(projections).isNotEmpty();
        assertThat(projections).anyMatch(p -> p.betrag() == 150.00);
    }

    @Test
    void can_findAllMinimalProjectedBy() {
        repository.saveAndFlush(rechnung);
        List<RechnungDtos.Minimal> projections = repository.findAllMinimalBy();
        assertThat(projections).isNotEmpty();
        assertThat(projections).anyMatch(p -> p.betrag().equals(150.00) && !p.bezahlt());
    }
}