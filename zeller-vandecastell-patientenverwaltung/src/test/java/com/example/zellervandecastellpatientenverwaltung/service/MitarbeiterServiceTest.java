package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Mitarbeiter;
import com.example.zellervandecastellpatientenverwaltung.persistence.MitarbeiterRepository;
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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MitarbeiterServiceTest {
    private @Mock MitarbeiterRepository mitarbeiterRepository;

    private MitarbeiterService mitarbeiterService;

    private Mitarbeiter mitarbeiter;



    @BeforeEach
    void setUp(){
        assumeThat(mitarbeiterRepository).isNotNull();
        mitarbeiterService = new MitarbeiterService(mitarbeiterRepository);

        mitarbeiter = Mitarbeiter.builder()
                .name("Max Mustermann")
                .svnr(1234567890L)
                .gehalt(2000L)
                .email(new Email("max@muster.at"))
                .gebDatum(LocalDate.of(1970, 1, 1))
                .mitarbeiterID(new Mitarbeiter.MitarbeiterID(1L))
                .build();
    }

    @Test
    void cant_create_mitarbeiter_with_missing_name() {
        assertThatThrownBy(() -> mitarbeiterService.createMitarbeiter("", LocalDate.of(1980, 1, 1), 123L, 1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name darf nicht null oder leer sein.");
    }

    @Test
    void cant_create_mitarbeiter_with_missing_svnr() {
        assertThatThrownBy(() -> mitarbeiterService.createMitarbeiter("Max Mustermann", LocalDate.of(1980, 1, 1), null, 1200L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SVNR darf nicht null sein.");
    }

    @Test
    void can_create_mitarbeiter() {
        String name = "Max Mustermann";
        LocalDate gebDatum = LocalDate.of(1980, 1, 1);
        Long svnr = 1234567890L;
        Long gehalt = 1100L;

        when(mitarbeiterRepository.save(any(Mitarbeiter.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mitarbeiter mitarbeiter = Mitarbeiter.builder()
                .name(name)
                .gebDatum(gebDatum)
                .svnr(svnr)
                .gehalt(gehalt)
                .email(new Email("max@muster.at"))
                .build();

        // when(mitarbeiterRepository.save(mitarbeiter)).thenReturn(mitarbeiter);

        Mitarbeiter createdMitarbeiter = mitarbeiterService.createMitarbeiter(name, gebDatum, svnr, gehalt);

        assertThat(createdMitarbeiter).isNotNull();
        assertThat(createdMitarbeiter.getName()).isEqualTo(name);
    }

    @Test
    void cant_create_mitarbeiter_with_invalid_gehalt() {
        assertThatThrownBy(() -> mitarbeiterService.createMitarbeiter("Max Mustermann", LocalDate.of(1980, 1, 1), 1234567890L, -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Es gibt kein negatives Gehalt");
    }

    @Test
    void getAll_should_return_all_mitarbeiter() {
        when(mitarbeiterRepository.findAll()).thenReturn(List.of(mitarbeiter));
        var mitarbeiterList = mitarbeiterService.getAll();

        assertThat(mitarbeiterList).isNotEmpty();
        assertThat(mitarbeiterList.get(0).getName()).isEqualTo("Max Mustermann");
    }

    @Test
    void getMitarbeiter_should_return_mitarbeiter_by_id() {
        when(mitarbeiterRepository.findById(mitarbeiter.getMitarbeiterID())).thenReturn(Optional.of(mitarbeiter));

        var result = mitarbeiterService.getMitarbeiter(mitarbeiter.getMitarbeiterID().id());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Max Mustermann");
    }
}