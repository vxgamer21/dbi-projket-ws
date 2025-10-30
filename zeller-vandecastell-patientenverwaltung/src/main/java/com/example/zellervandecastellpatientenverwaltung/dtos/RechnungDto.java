package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;

import java.time.LocalDateTime;

public record RechnungDto(String patientId, String arztId, double betrag, boolean bezahlt, LocalDateTime datum) {

    public RechnungDto(Rechnung r) {
        this(
                r.getPatientId(),
                r.getArztId(),
                r.getBetrag(),
                r.isBezahlt(),
                r.getDatum()
        );
    }
}
