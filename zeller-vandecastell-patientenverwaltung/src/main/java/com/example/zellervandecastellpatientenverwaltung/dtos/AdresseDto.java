package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;

public record AdresseDto(String strasse, String hausnummer, String plz, String stadt) {
    public AdresseDto(Adresse a){
        this(a.getStrasse(), a.getHausNr(), a.getPlz(), a.getStadt());
    }
}
