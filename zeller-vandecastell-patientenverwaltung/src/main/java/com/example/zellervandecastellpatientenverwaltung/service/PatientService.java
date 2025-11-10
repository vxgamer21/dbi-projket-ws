package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;
    private final BehandlungRepository behandlungRepository;
    private final EmbeddedDocumentMapper embeddedDocumentMapper;

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
        return patientRepository.findAll().stream()
                .map(this::prepareEmbeddedPatient)
                .toList();
    }

    public Optional<Patient> getPatient(String patientId) {
        return patientRepository.findById(patientId)
                .map(this::prepareEmbeddedPatient);
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

        Patient updatedPatient = patientRepository.save(patient);
        return prepareEmbeddedPatient(updatedPatient);
    }

    @Transactional
    public void deletePatient(String patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new IllegalArgumentException("Patient mit ID " + patientId + " existiert nicht.");
        }
        patientRepository.deleteById(patientId);
    }

    private Patient prepareEmbeddedPatient(Patient patient) {
        if (patient == null) {
            return null;
        }

        List<Behandlung> behandlungen = patient.getBehandlungen();
        if (behandlungen == null || behandlungen.isEmpty()) {
            patient.setBehandlungen(new ArrayList<>());
            return patient;
        }

        Patient embeddedSelf = embeddedDocumentMapper.toEmbeddedPatient(patient);

        Map<String, Behandlung> hydrated = loadHydratedBehandlungen(behandlungen);

        List<Behandlung> sanitized = new ArrayList<>();
        for (Behandlung behandlung : behandlungen) {
            if (behandlung == null) {
                continue;
            }

            Behandlung source = embeddedDocumentMapper.requiresHydration(behandlung) && behandlung.getId() != null
                    ? hydrated.getOrDefault(behandlung.getId(), behandlung)
                    : behandlung;

            Behandlung embedded = embeddedDocumentMapper.toEmbeddedBehandlung(source);
            if (embedded != null) {
                embedded.setPatient(embeddedSelf);
                sanitized.add(embedded);
            }
        }

        patient.setBehandlungen(sanitized);
        return patient;
    }

    private Map<String, Behandlung> loadHydratedBehandlungen(List<Behandlung> behandlungen) {
        List<String> ids = behandlungen.stream()
                .filter(embeddedDocumentMapper::requiresHydration)
                .map(Behandlung::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        return behandlungRepository.findAllById(ids).stream()
                .map(embeddedDocumentMapper::toEmbeddedBehandlung)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Behandlung::getId, Function.identity()));
    }
}
