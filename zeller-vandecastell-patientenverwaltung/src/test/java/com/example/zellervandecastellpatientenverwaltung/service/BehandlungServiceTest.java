package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BehandlungServiceTest {
    private @Mock BehandlungRepository behandlungRepository;
    private @Mock ArztRepository arztRepository;
    private @Mock PatientRepository patientRepository;
    private MockMvc mockMvc;


    private BehandlungService behandlungService;

    @BeforeEach
    void setUp(){
        assumeThat(behandlungRepository).isNotNull();
        assumeThat(arztRepository).isNotNull();
        assumeThat(patientRepository).isNotNull();

        behandlungService = new BehandlungService(behandlungRepository, arztRepository, patientRepository);
    }

    @Test
    void cant_create_behandlung_with_missing_arzt() {
        when(arztRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> behandlungService.createBehandlung(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Diagnose", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Arzt not found");
    }

    @Test
    void cant_create_behandlung_with_missing_patient() {
        when(arztRepository.findById(any())).thenReturn(Optional.of(mock(Arzt.class)));
        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> behandlungService.createBehandlung(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Diagnose", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Patient not found");
    }

    @Test
    void can_get_all_behandlungen() {
        List<Behandlung> expectedList = List.of(mock(Behandlung.class), mock(Behandlung.class));
        when(behandlungRepository.findAll()).thenReturn(expectedList);

        List<Behandlung> result = behandlungService.getAll();
        assertThat(result).hasSize(2);
        verify(behandlungRepository, times(1)).findAll();
    }

    @Test
    void can_get_behandlung_by_id() {
        Behandlung behandlung = mock(Behandlung.class);
        when(behandlungRepository.findById(any())).thenReturn(Optional.of(behandlung));

        Optional<Behandlung> result = behandlungService.getBehandlung(1L);
        assertThat(result).isPresent().contains(behandlung);
    }

    @Test
    void get_behandlung_returns_empty_when_not_found() {
        when(behandlungRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Behandlung> result = behandlungService.getBehandlung(1L);
        assertThat(result).isEmpty();
    }


}