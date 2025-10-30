package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Arztpraxis;

public record ArztpraxisDto(
        String id,
        String name,
        boolean istKassenarzt,
        AdresseDto adresse,
        TelefonNummerDto telefonNummer,
        String arztId
) {
    public ArztpraxisDto(Arztpraxis a) {
        this(
                a.getId(),
                a.getName(),
                a.getIstKassenarzt(),
                new AdresseDto(a.getAdresse()),
                new TelefonNummerDto(a.getTelefonNummer()),
                a.getId()
        );
    }
}
