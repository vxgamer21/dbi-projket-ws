package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.RechnungRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RechnungServiceTest {
    private @Mock ArztRepository arztRepository;
    private @Mock PatientRepository patientRepository;
    private @Mock RechnungRepository rechnungRepository;

    private RechnungService rechnungService;

    @BeforeEach
    void setUp(){
        assumeThat(arztRepository).isNotNull();
        assumeThat(patientRepository).isNotNull();


        rechnungService = new RechnungService(arztRepository, patientRepository,rechnungRepository);
    }

    @Test
    void cant_create_rechnung_with_missing_arzt(){
        when(arztRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rechnungService.createRechnung(11L,111L,234, LocalDateTime.of(2021, 1, 1, 8, 0), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Arzt not found");
    }

    @Test
    void cant_create_rechnung_with_missing_patient(){
        when(arztRepository.findById(any())).thenReturn(Optional.of(new Arzt()));
        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rechnungService.createRechnung(11L, 111L, 234, LocalDateTime.of(2021, 1, 1, 8, 0), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Patient not found");
    }

    @Test
    void can_create_rechnung_successfully(){
        Arzt arzt = new Arzt();
        Patient patient = new Patient();

        when(arztRepository.findById(any())).thenReturn(Optional.of(arzt));
        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(rechnungRepository.save(any())).thenReturn(new Rechnung());

        Rechnung result = rechnungService.createRechnung(11L, 111L, 234, LocalDateTime.of(2021, 1, 1, 8, 0), true);

        assertThat(result).isNotNull();
    }
}