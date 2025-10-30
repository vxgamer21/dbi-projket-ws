package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlungsraum;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungsraumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BehandlungsRaumServiceTest {
    private @Mock BehandlungRepository behandlungRepository;
    private @Mock BehandlungsraumRepository behandlungsRaumRepository;

    private BehandlungsRaumService behandlungsRaumService;


    @BeforeEach
    void setUp(){
        assumeThat(behandlungRepository).isNotNull();

        behandlungsRaumService = new BehandlungsRaumService(behandlungRepository, behandlungsRaumRepository);
    }

    @Test
    void cant_create_behandlungsraum_with_missing_ausstattung() {
        assertThatThrownBy(() -> behandlungsRaumService.createBehandlungsraum(1L, "", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ausstattung darf nicht null oder leer sein.");
    }

    @Test
    void cant_create_behandlungsraum_with_missing_behandlung() {
        when(behandlungRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> behandlungsRaumService.createBehandlungsraum(1L, "Moderne Ausstattung", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Behandlung not found");
    }

    @Test
    void can_get_all_behandlungsraeume() {
        List<Behandlungsraum> expectedList = List.of(mock(Behandlungsraum.class), mock(Behandlungsraum.class));
        when(behandlungsRaumRepository.findAll()).thenReturn(expectedList);

        List<Behandlungsraum> result = behandlungsRaumService.getAll();
        assertThat(result).hasSize(2);
        verify(behandlungsRaumRepository, times(1)).findAll();
    }

    @Test
    void can_get_behandlungsraum_by_id() {
        Behandlungsraum behandlungsraum = mock(Behandlungsraum.class);
        when(behandlungsRaumRepository.findById(any())).thenReturn(Optional.of(behandlungsraum));

        Optional<Behandlungsraum> result = behandlungsRaumService.getBehandlungsraum(1L);
        assertThat(result).isPresent().contains(behandlungsraum);
    }

    @Test
    void get_behandlungsraum_returns_empty_when_not_found() {
        when(behandlungsRaumRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Behandlungsraum> result = behandlungsRaumService.getBehandlungsraum(1L);
        assertThat(result).isEmpty();
    }
}