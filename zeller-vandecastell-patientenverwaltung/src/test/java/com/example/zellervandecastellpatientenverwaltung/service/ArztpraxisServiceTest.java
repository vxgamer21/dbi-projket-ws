package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Arztpraxis;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztpraxisRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class ArztpraxisServiceTest {

    private @Mock ArztRepository arztRepository;
    private @Mock ArztpraxisRepository arztpraxisRepository;

    private ArztpraxisService arztpraxisService;

    @BeforeEach
    void setUp() {
        arztpraxisService = new ArztpraxisService(arztRepository, arztpraxisRepository);
    }

    @Test
    void cant_create_arztpraxis_with_missing_arzt() {
        when(arztRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> arztpraxisService.createArztpraxis("Test", true, 1L, 1L, 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Arzt not found");
    }

    @Test
    void can_get_all_arztpraxen() {
        List<Arztpraxis> expectedList = List.of(mock(Arztpraxis.class), mock(Arztpraxis.class));
        when(arztpraxisRepository.findAll()).thenReturn(expectedList);

        List<Arztpraxis> result = arztpraxisService.getAll();
        assertThat(result).hasSize(2);
        verify(arztpraxisRepository,times(1)).findAll();
    }

    @Test
    void can_get_arztpraxis_by_id() {
        Arztpraxis arztpraxis = mock(Arztpraxis.class);
        when(arztpraxisRepository.findById(any())).thenReturn(Optional.of(arztpraxis));

        Optional<Arztpraxis> result = arztpraxisService.getArztpraxis(1L);
        assertThat(result).isPresent().contains(arztpraxis);
    }

    @Test
    void get_arztpraxis_returns_empty_when_not_found() {
        when(arztpraxisRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Arztpraxis> result = arztpraxisService.getArztpraxis(1L);
        assertThat(result).isEmpty();
    }
}