package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Mitarbeiter;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MitarbeiterDto(String name, LocalDate gebDatum, Long svnr, Long gehalt) {
    public MitarbeiterDto(@NotNull Mitarbeiter mitarbeiter) {

        this(mitarbeiter.getName(),
                mitarbeiter.getGebDatum(),
                mitarbeiter.getSvnr(),
                mitarbeiter.getGehalt());
    }
}

