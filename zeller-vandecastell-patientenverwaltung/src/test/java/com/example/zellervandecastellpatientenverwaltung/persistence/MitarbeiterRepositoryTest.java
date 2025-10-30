package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Mitarbeiter;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)

class MitarbeiterRepositoryTest {
    @Autowired
    private MitarbeiterRepository repository;
    private Mitarbeiter mitarbeiter;
    private String name;

    @BeforeEach
    void setUp() {
        name = "Max Mustermann";
        mitarbeiter = Mitarbeiter.builder()
                .name(name)
                .svnr(12345678889L)
                .gehalt(2000L)
                .email(new Email("max@muster.at"))
                .gebDatum(LocalDate.of(1970, 1, 1))
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
    }

    @Test
    void can_save() {
        assertThat(repository.saveAndFlush(mitarbeiter).getMitarbeiterID()).isNotNull();
    }

    @Test
    void can_findbyName() {
        repository.saveAndFlush(mitarbeiter);
        var read = repository.findByNameIgnoreCase(name);
        assertThat(read).isNotEmpty();
        assertThat(read.get().getName()).isEqualTo(name);
    }

    @Test
    void can_findAllMinimal() {
        repository.saveAndFlush(mitarbeiter);
        var result = repository.findAllMinimalBy();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).name()).isEqualTo(name);
        assertThat(result.get(0).gehalt()).isEqualTo(2000L);
    }

    @Test
    void can_findAllProjectedBy() {
        repository.saveAndFlush(mitarbeiter);
        var result = repository.findAllProjectedBy();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).name()).isEqualTo(name);
        assertThat(result.get(0).svnr()).isEqualTo(12345678889L);
    }
}