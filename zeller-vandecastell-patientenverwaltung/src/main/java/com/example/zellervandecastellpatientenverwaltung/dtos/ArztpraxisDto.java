package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Arztpraxis;

public record ArztpraxisDto(String name, boolean istKassenarzt, AdresseDto adresse, TelefonNummerDto telefonNummerDto, ArztDto arztDto) {
    public ArztpraxisDto(Arztpraxis a){
        this(a.getName(), a.getIstKassenarzt(), new AdresseDto(a.getAdresse()), new TelefonNummerDto(a.getTelefonNummer()), new ArztDto(a.getAerzte().getFirst()));
    }
}

