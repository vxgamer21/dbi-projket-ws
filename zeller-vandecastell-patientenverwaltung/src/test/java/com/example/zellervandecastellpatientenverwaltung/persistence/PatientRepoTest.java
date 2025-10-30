package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.TestcontainersConfiguration;
import com.example.zellervandecastellpatientenverwaltung.domain.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)

class PatientRepoTest {

    private @Autowired PatientRepository repository;

    @Test
    void can_save(){
        //Arrange / given
        var patient = Patient.builder()
                .name("Guid Orschstaller")
                .svnr(213556331L)
                .gebDatum(LocalDate.of(1970, 1, 1))
                .versicherungsart(Versicherungsart.PRIVAT)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();
        //Act / when
        var saved = repository.saveAndFlush(patient);
        //Assert / then
        assertThat(saved.getPatientID()).isNotNull();
    }

    @Test
    void can_findbyName() {
        //Arrange
        String name = "Alexander Dragovic";
        var patient =
                Patient.builder()
                .name("Alexander Dragovic")
                        .svnr(213556331L)
                        .gebDatum(LocalDate.of(1970, 1, 1))
                        .versicherungsart(Versicherungsart.PRIVAT)
                        .apiKey(ApiKeyGenerator.generateApiKey())
                        .build();
        var saved = repository.saveAndFlush(patient);
        //Act
        var read = repository.findByNameIgnoreCase(name);
        //Assert
        assertThat(read).isNotEmpty();
        assertThat(read.get().getName()).isEqualTo(name);
    }

    @Test
    void can_findAllMinimal() {
        var patient = FixturesFactory.PatientMaxMustermann();

        repository.saveAndFlush(patient);
        var read = repository.findAllMinimalBy();

        assertThat(read).isNotEmpty();
    }

    @Test
    void can_findAllProjectedBy() {
        var patient = FixturesFactory.PatientMaxMustermann();

        repository.saveAndFlush(patient);
        var read = repository.findAllProjectedBy();

        assertThat(read).isNotEmpty();
    }
}