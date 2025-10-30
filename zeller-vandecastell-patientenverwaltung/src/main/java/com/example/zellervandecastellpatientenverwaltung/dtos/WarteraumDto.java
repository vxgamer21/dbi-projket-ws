package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummerArt;
import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;

public record WarteraumDto(int anzahlSitzplaetze) {
    public WarteraumDto(Warteraum w) {
        this(w.getAnzahlSitzplaetze());
    }
}

