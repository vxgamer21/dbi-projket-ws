package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ArztDto(String name, LocalDate gebDatum, Long svnr) {
    public ArztDto(@NotNull Arzt arzt) {

        this(arzt.getName(),
                arzt.getGebDatum(),
                arzt.getSvnr());
    }
}
