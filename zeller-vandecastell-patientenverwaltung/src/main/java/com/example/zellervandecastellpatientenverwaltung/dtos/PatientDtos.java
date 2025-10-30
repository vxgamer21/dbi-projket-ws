package com.example.zellervandecastellpatientenverwaltung.dtos;


import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;

import java.time.LocalDate;

public class PatientDtos {

    public record Minimal(String name, Versicherungsart versicherungsart) {
    }

    public record Full(String name, Long svnr, LocalDate gebDatum, Versicherungsart versicherungsart) {
    }
}
