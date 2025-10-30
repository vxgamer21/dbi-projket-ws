package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import com.example.zellervandecastellpatientenverwaltung.persistence.WarteraumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WarteRaumServiceTest {
    private @Mock WarteraumRepository warteraumRepository;

    private WarteRaumService warteRaumService;


    @BeforeEach
    void setUp(){
        assumeThat(warteraumRepository).isNotNull();

        warteRaumService = new WarteRaumService(warteraumRepository);
    }

    @Test
    void cant_create_warteraum_with_wrong_sitzplaetze(){

        assertThatThrownBy(() -> warteRaumService.createWarteraum(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Anzahl Sitzplätze muss einen gültigen Wert haben!");
    }

    @Test
    void createWarteraum_shouldSaveAndReturnWarteraum() {
        Warteraum warteRaum = Warteraum.builder()
                .warteraumId(new Warteraum.WarteraumId(1L))
                .anzahlSitzplaetze(22)
                .build();
        when(warteraumRepository.save(any(Warteraum.class))).thenReturn(warteRaum);

        Warteraum result = warteRaumService.createWarteraum(22);

        assertThat(result).isEqualTo(warteRaum);
        verify(warteraumRepository).save(any(Warteraum.class));
    }

    @Test
    void getAll_shouldReturnListOfWarteraeume() {
        List<Warteraum> warteraeume = List.of(
                Warteraum.builder()
                        .warteraumId(new Warteraum.WarteraumId(1L))
                        .anzahlSitzplaetze(22)
                        .build()
        );
        when(warteraumRepository.findAll()).thenReturn(warteraeume);

        List<Warteraum> result = warteRaumService.getAll();

        assertThat(result).hasSize(1).containsExactlyElementsOf(warteraeume);
    }

    @Test
    void getWarteraum_shouldReturnWarteraum_whenIdExists() {
        Warteraum warteRaum = Warteraum.builder()
                .warteraumId(new Warteraum.WarteraumId(1L))
                .anzahlSitzplaetze(22)
                .build();
        when(warteraumRepository.findById(any())).thenReturn(Optional.of(warteRaum));

        Optional<Warteraum> result = warteRaumService.getWarteraum(new Warteraum.WarteraumId(1L).id());

        assertThat(result).isPresent().contains(warteRaum);
    }

    @Test
    void getWarteraum_shouldReturnEmpty_whenIdDoesNotExist() {
        when(warteraumRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Warteraum> result = warteRaumService.getWarteraum(new Warteraum.WarteraumId(1L).id());

        assertThat(result).isEmpty();
    }
}