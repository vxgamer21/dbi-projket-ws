package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztpraxisDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztpraxisDtos;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class ArztpraxisRepositoryTest {

    @Autowired
    private ArztpraxisRepository repository;

    private Arztpraxis arztpraxis;
    private Adresse adresse;
    private String name;
    private TelefonNummer telefonNummer;
    private Warteraum wraum;
    private Behandlungsraum braum;
    private String apiKey;

    @BeforeEach
    void setUp() {
        telefonNummer = FixturesFactory.StandardMobil();

        adresse = FixturesFactory.Ringstrasse();

        apiKey = ApiKeyGenerator.generateApiKey();

        arztpraxis = Arztpraxis.builder()
                .name("HNO am Ring")
                .istKassenarzt(true)
                .adresse(adresse)
                .telefonNummer(telefonNummer)
                .apiKey(apiKey)
                .build();

        braum = FixturesFactory.BehandlungsRaum1Frei();

        wraum = FixturesFactory.WarteRaum22Seats();

        name = "HNO am Ring";
        repository.saveAndFlush(arztpraxis);

    }

    @Test
    void can_save() {
        var saved = repository.saveAndFlush(arztpraxis);
        // Assert / then
        assertThat(saved.getArztpraxisId()).isNotNull();
    }

    @Test
    void can_findbyName() {
        var read = repository.findByNameIgnoreCase(name);
        // Assert / then
        assertThat(read).isNotEmpty();
        assertThat(read.get().getName()).isEqualTo(name);
    }

//    @Test
//    void can_findAllProjectedBy() {
//        List<ArztpraxisDto> projections = repository.findAllProjectedBy();
//        assertThat(projections).isNotEmpty();
//        assertThat(projections).anyMatch(p -> p.name().equals("HNO am Ring"));
//    }
//
//    @Test
//    void can_findAllMinimalProjectedBy() {
//        List<ArztpraxisDtos.Minimal> projections = repository.findAllMinimalBy();
//        assertThat(projections).isNotEmpty();
//        assertThat(projections).anyMatch(p -> p.name().equals("HNO am Ring"));
//    }

}