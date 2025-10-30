package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BehandlungDto(LocalDateTime beginn, LocalDateTime ende, String diagnose, ArztDto arzt, PatientDto patientId) {

    public BehandlungDto (Behandlung b) {
        this(b.getBeginn(),
                b.getEnde(),
                b.getDiagnose(),
                new ArztDto(b.getArzt()),
                new PatientDto(b.getPatient()));
    }
}
