package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import com.example.zellervandecastellpatientenverwaltung.domain.Medikament;
import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class EmbeddedDocumentMapper {

    public Behandlung toEmbeddedBehandlung(Behandlung behandlung) {
        if (behandlung == null) {
            return null;
        }

        Behandlung.BehandlungBuilder builder = Behandlung.builder()
                .id(behandlung.getId())
                .behandlungsraumId(behandlung.getBehandlungsraumId())
                .medikamente(copyMedikamente(behandlung.getMedikamente()))
                .beginn(behandlung.getBeginn())
                .ende(behandlung.getEnde())
                .diagnose(behandlung.getDiagnose())
                .apiKey(behandlung.getApiKey());

        builder.arzt(toEmbeddedArzt(behandlung.getArzt()));
        builder.patient(toEmbeddedPatient(behandlung.getPatient()));

        return builder.build();
    }

    public Patient toEmbeddedPatient(Patient patient) {
        if (patient == null) {
            return null;
        }

        return Patient.builder()
                .id(patient.getId())
                .name(patient.getName())
                .gebDatum(patient.getGebDatum())
                .svnr(patient.getSvnr())
                .adresse(patient.getAdresse())
                .telefonNummer(patient.getTelefonNummer())
                .versicherungsart(patient.getVersicherungsart())
                .apiKey(patient.getApiKey())
                .behandlungen(new ArrayList<>())
                .build();
    }

    public Arzt toEmbeddedArzt(Arzt arzt) {
        if (arzt == null) {
            return null;
        }

        return Arzt.builder()
                .id(arzt.getId())
                .name(arzt.getName())
                .gebDatum(arzt.getGebDatum())
                .svnr(arzt.getSvnr())
                .adresse(arzt.getAdresse())
                .telefonNummer(arzt.getTelefonNummer())
                .fachgebiet(arzt.getFachgebiet())
                .email(arzt.getEmail())
                .apiKey(arzt.getApiKey())
                .behandlungen(new ArrayList<>())
                .build();
    }

    public boolean requiresHydration(Behandlung behandlung) {
        if (behandlung == null) {
            return false;
        }

        if (behandlung.getArzt() == null || behandlung.getPatient() == null) {
            return true;
        }

        // Ensure essential descriptive fields exist when embedded documents come from legacy references
        return Objects.isNull(behandlung.getDiagnose())
                && Objects.isNull(behandlung.getBeginn())
                && Objects.isNull(behandlung.getEnde());
    }

    private List<Medikament> copyMedikamente(List<Medikament> medikamente) {
        if (medikamente == null) {
            return null;
        }

        return new ArrayList<>(medikamente);
    }
}

