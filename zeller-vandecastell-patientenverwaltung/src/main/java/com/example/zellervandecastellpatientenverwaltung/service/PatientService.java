package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public Patient createPatient(String name, LocalDate gebDatum, Long svnr,
                                 Versicherungsart versicherungsart, Adresse adresse,
                                 TelefonNummer telefonnummer) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht null oder leer sein.");
        }
        if (svnr == null) {
            throw new IllegalArgumentException("SVNR darf nicht null sein.");
        }
        if (versicherungsart == null) {
            throw new IllegalArgumentException("Versicherungsart darf nicht null sein.");
        }

        String apiKey = ApiKeyGenerator.generateApiKey();
        System.out.println("🔑 Neuer API-Key: " + apiKey);

        Patient patient = Patient.builder()
                .name(name)
                .gebDatum(gebDatum)
                .svnr(svnr)
                .versicherungsart(versicherungsart)
                .adresse(adresse)
                .telefonNummer(telefonnummer)
                .apiKey(apiKey)
                .build();

        return patientRepository.save(patient);
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatient(String patientId) {
        return patientRepository.findById(patientId);
    }

    @Transactional
    public Patient updatePatient(String patientId, String name, LocalDate gebDatum, Long svnr,
                                 Versicherungsart versicherungsart, Adresse adresse, TelefonNummer telefonnummer) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient mit ID " + patientId + " nicht gefunden"));

        if (name != null) patient.setName(name);
        if (gebDatum != null) patient.setGebDatum(gebDatum);
        if (svnr != null) patient.setSvnr(svnr);
        if (versicherungsart != null) patient.setVersicherungsart(versicherungsart);
        if (adresse != null) patient.setAdresse(adresse);
        if (telefonnummer != null) patient.setTelefonNummer(telefonnummer);

        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(String patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new IllegalArgumentException("Patient mit ID " + patientId + " existiert nicht.");
        }
        patientRepository.deleteById(patientId);
    }
}
