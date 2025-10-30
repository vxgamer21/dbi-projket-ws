package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummerArt;

public record TelefonNummerDto(String lkennzahl, String okennzahl, String rufnummer, TelefonNummerArt art) {
    public TelefonNummerDto (TelefonNummer t)
    {
        this(t.getLkennzahl(), t.getOkennzahl(), t.getRufnummer(), t.getArt());
    }
}
