package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientDto(String name, LocalDate gebDatum, Long svnr, Versicherungsart versicherungsart) {

    public PatientDto(@NotNull Patient patient) {
        this(patient.getName(), patient.getGebDatum(), patient.getSvnr(), patient.getVersicherungsart());
    }
}
