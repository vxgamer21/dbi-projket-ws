// src/test/java/com/example/zellervandecastellpatientenverwaltung/persistence/ArztRepositoryTest.java

package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@DataJpaTest
@Import(TestcontainersConfiguration.class)

class ArztRepositoryTest {
    private @Autowired ArztRepository repository;


    @Test
    void can_save() {
        String apiKey = ApiKeyGenerator.generateApiKey();
        // Arrange
        var arzt = Arzt.builder()
                .name("Dr. Müller")
                .fachgebiet(Fachgebiet.HNO)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .svnr(1234567890L)
                .email(new Email("max@muster.at"))
                .apiKey(apiKey)
                .build();
        // Act
        var saved = repository.saveAndFlush(arzt);
        // Assert
        assertThat(saved.getArztId()).isNotNull();
    }

    @Test
    void can_findbyName() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        var name = "Dr. Müller";
        var arzt = Arzt.builder()
                .name(name)
                .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .svnr(1234567890L)
                .email(new Email("max@muster.at"))
                .apiKey(apiKey)
                .build();
        repository.saveAndFlush(arzt);


        var saved = repository.findById(arzt.getArztId());
        assertThat(saved).isNotEmpty();
        System.out.println("Saved Arzt: " + saved.get());


        var read = repository.findByNameIgnoreCase(name);


        assertThat(read).isNotEmpty();
        assertThat(read.get().getName()).isEqualTo(name);
    }

    @Test
    void can_findAllMinmal() {

        var arzt = FixturesFactory.ArztAllgemein();

        var read = repository.findAllMinimalBy();

        assertThat(read).isNotEmpty();
    }

    @Test
    void can_findAllProjectedBy() {

        var arzt = FixturesFactory.ArztAllgemein();

        repository.saveAndFlush(arzt);

        var read = repository.findAllProjectedBy();

        assertThat(read).isNotEmpty();
    }
}