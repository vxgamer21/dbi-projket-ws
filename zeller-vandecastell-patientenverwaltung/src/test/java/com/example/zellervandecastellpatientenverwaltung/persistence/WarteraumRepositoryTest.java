package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.Raum;
import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import com.example.zellervandecastellpatientenverwaltung.dtos.WarteraumDto;
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

class WarteraumRepositoryTest{

    @Autowired
    private WarteraumRepository repository;
    private Warteraum raum;

    @BeforeEach
    void setUp(){
        raum = Warteraum.builder()
                .name("Warteraum 1")
                .anzahlSitzplaetze(22)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
    }

    @Test
    void can_save(){
        assertThat(repository.saveAndFlush(raum).getWarteraumId()).isNotNull();
    }

    @Test
    void can_findById(){
        repository.saveAndFlush(raum);
        assertThat(repository.findById(raum.getWarteraumId())).isNotEmpty();
        assertThat(repository.findById(raum.getWarteraumId()).get().getWarteraumId()).isEqualTo(raum.getWarteraumId());
    }


    @Test
    void can_findAllProjectedBy() {
        repository.saveAndFlush(raum);
        List<WarteraumDto> projections = repository.findAllProjectedBy();
        assertThat(projections).isNotEmpty();
        assertThat(projections).anyMatch(p -> p.anzahlSitzplaetze() == 22);
    }

}