package com.example.zellervandecastellpatientenverwaltung.dtos;

public class ArztpraxisDtos {
    public record Minimal(String name, boolean istKassenarzt, AdresseDto adresse, String telefonNummer) {}
}

