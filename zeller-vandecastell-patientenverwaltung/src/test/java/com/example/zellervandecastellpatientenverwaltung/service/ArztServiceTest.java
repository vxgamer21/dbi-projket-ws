package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
class ArztServiceTest {
    private @Mock ArztRepository arztRepository;

    private ArztService arztService;

    @BeforeEach
    void setUp() {
        arztService = new ArztService(arztRepository);
    }

    @Test
    void cant_create_arzt_with_null_name() {
        assertThatThrownBy(() -> arztService.createArzt(
                null,
                LocalDate.now(),
                123456L,
                Fachgebiet.ALLGEMEINMEDIZIN,
                new Adresse("Teststraße", "Teststadt", "12345", "1"),
                new TelefonNummer("0043", "1", "1234567", TelefonNummerArt.MOBIL),
                new Email("test@example.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name darf nicht null oder leer sein.");
    }

    @Test
    void cant_create_arzt_with_empty_name() {
        assertThatThrownBy(() -> arztService.createArzt(
                "",
                LocalDate.now(),
                123456L,
                Fachgebiet.ALLGEMEINMEDIZIN,
                new Adresse("Teststraße", "Teststadt", "12345", "1"),
                new TelefonNummer("0043", "1", "1234567", TelefonNummerArt.MOBIL),
                new Email("test@example.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name darf nicht null oder leer sein.");
    }

    @Test
    void cant_create_arzt_with_null_svnr() {
        assertThatThrownBy(() -> arztService.createArzt(
                "Dr. Mustermann",
                LocalDate.now(),
                null,
                Fachgebiet.ALLGEMEINMEDIZIN,
                new Adresse("Teststraße", "Teststadt", "12345", "1"),
                new TelefonNummer("0043", "1", "1234567", TelefonNummerArt.MOBIL),
                new Email("test@example.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SVNR darf nicht null sein.");
    }

    @Test
    void cant_create_arzt_with_null_fachgebiet() {
        assertThatThrownBy(() -> arztService.createArzt(
                "Dr. NullFach",
                LocalDate.now(),
                123456L,
                null,
                new Adresse("Teststraße", "Teststadt", "12345", "1"),
                new TelefonNummer("0043", "1", "1234567", TelefonNummerArt.MOBIL),
                new Email("test@example.com")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Fachgebiet darf nicht null sein.");
    }


    @Test
    void can_get_all_aerzte() {
        List<Arzt> expectedList = List.of(mock(Arzt.class), mock(Arzt.class));
        when(arztRepository.findAll()).thenReturn(expectedList);

        List<Arzt> result = arztService.getAll();
        assertThat(result).hasSize(2);
        verify(arztRepository, times(1)).findAll();
    }

    @Test
    void can_get_arzt_by_id() {
        Arzt arzt = mock(Arzt.class);
        when(arztRepository.findById(any())).thenReturn(Optional.of(arzt));

        Optional<Arzt> result = arztService.getArzt(1L);
        assertThat(result).isPresent().contains(arzt);
    }

    @Test
    void get_arzt_throws_NotFoundException_when_not_found() {
        when(arztRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> arztService.getArzt(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Arzt mit ID 1 nicht gefunden");
    }

}