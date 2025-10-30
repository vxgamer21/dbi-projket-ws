package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;

import java.time.LocalDateTime;

public record BehandlungDto(LocalDateTime beginn, LocalDateTime ende, String diagnose, String arztId, String patientId) {

    public BehandlungDto(Behandlung b) {
        this(
                b.getBeginn(),
                b.getEnde(),
                b.getDiagnose(),
                b.getArztId(),
                b.getPatientId()
        );
    }
}
