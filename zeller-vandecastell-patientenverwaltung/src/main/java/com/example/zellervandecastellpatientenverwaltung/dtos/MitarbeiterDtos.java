package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;

import java.time.LocalDate;

public class MitarbeiterDtos {

    public record Minimal(String name, Long gehalt) {}

    public record Full(String name, Long svnr, Long gehalt, Email email, LocalDate gebDatum) {}
}
