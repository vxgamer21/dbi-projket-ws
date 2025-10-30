package com.example.zellervandecastellpatientenverwaltung.dtos;

public class RechnungDtos {

    public record Minimal(Long rechnungId, Double betrag, boolean bezahlt) {}
    
}
