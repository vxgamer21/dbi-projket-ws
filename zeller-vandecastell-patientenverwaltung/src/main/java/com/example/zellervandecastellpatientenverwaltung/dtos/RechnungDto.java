package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;

import java.time.LocalDateTime;

public record RechnungDto(PatientDto patient, ArztDto arzt, double betrag, boolean bezahlt, LocalDateTime datum) {

    public RechnungDto(Rechnung r) {
        this(new PatientDto(r.getPatient()),
                new ArztDto(r.getArzt()),
                r.getBetrag(),
                r.isBezahlt(),
                r.getDatum());
    }
}
