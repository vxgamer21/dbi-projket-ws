package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;

import java.time.LocalDateTime;

public record RechnungDto(String patientId, String arztId, double betrag, boolean bezahlt, LocalDateTime datum) {

    public RechnungDto(Rechnung r) {
        this(
                r.getPatient() != null ? r.getPatient().getId() : null,
                r.getArzt() != null ? r.getArzt().getId() : null,
                r.getBetrag(),
                r.isBezahlt(),
                r.getDatum()
        );
    }
}
