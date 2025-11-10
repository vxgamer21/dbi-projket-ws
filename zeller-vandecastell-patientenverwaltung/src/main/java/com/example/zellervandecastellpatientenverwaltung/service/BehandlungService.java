package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BehandlungService {

    private final BehandlungRepository behandlungRepository;
    private final ArztRepository arztRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Behandlung createBehandlung(LocalDateTime beginn, LocalDateTime ende, String diagnose, String arztId, String patientId) {
        Arzt arzt = arztRepository.findById(arztId)
                .orElseThrow(() -> new IllegalArgumentException("Arzt not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        String apiKey = ApiKeyGenerator.generateApiKey();

        Behandlung behandlung = Behandlung.builder()
                .beginn(beginn)
                .ende(ende)
                .diagnose(diagnose)
                .arzt(arzt)
                .patient(patient)
                .apiKey(apiKey)
                .build();

        Behandlung saved = behandlungRepository.save(behandlung);

        linkBehandlungWithPatient(patient, saved);
        linkBehandlungWithArzt(arzt, saved);

        return saved;
    }

    public List<Behandlung> getAll() {
        return behandlungRepository.findAll();
    }

    public Optional<Behandlung> getBehandlung(String behandlungId) {
        return behandlungRepository.findById(behandlungId);
    }

    @Transactional
    public Behandlung updateBehandlung(String behandlungId, LocalDateTime beginn, LocalDateTime ende, String diagnose, String arztId, String patientId) {
        Behandlung behandlung = behandlungRepository.findById(behandlungId)
                .orElseThrow(() -> new IllegalArgumentException("Behandlung not found"));

        Patient previousPatient = behandlung.getPatient();
        Arzt previousArzt = behandlung.getArzt();

        if (beginn != null) behandlung.setBeginn(beginn);
        if (ende != null) behandlung.setEnde(ende);
        if (diagnose != null) behandlung.setDiagnose(diagnose);

        if (arztId != null) {
            Arzt arzt = arztRepository.findById(arztId)
                    .orElseThrow(() -> new IllegalArgumentException("Arzt not found"));
            behandlung.setArzt(arzt);
        }

        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
            behandlung.setPatient(patient);
        }

        Behandlung saved = behandlungRepository.save(behandlung);

        if (!Objects.equals(getPatientId(previousPatient), getPatientId(saved.getPatient()))) {
            unlinkBehandlungFromPatient(previousPatient, saved.getId());
        }
        linkBehandlungWithPatient(saved.getPatient(), saved);

        if (!Objects.equals(getArztId(previousArzt), getArztId(saved.getArzt()))) {
            unlinkBehandlungFromArzt(previousArzt, saved.getId());
        }
        linkBehandlungWithArzt(saved.getArzt(), saved);

        return saved;
    }

    @Transactional
    public void deleteBehandlung(String behandlungId) {
        Behandlung behandlung = behandlungRepository.findById(behandlungId)
                .orElseThrow(() -> new IllegalArgumentException("Behandlung not found"));
        behandlungRepository.delete(behandlung);

        unlinkBehandlungFromPatient(behandlung.getPatient(), behandlung.getId());
        unlinkBehandlungFromArzt(behandlung.getArzt(), behandlung.getId());
    }

    private void linkBehandlungWithPatient(Patient patient, Behandlung behandlung) {
        if (patient == null || behandlung == null) {
            return;
        }

        List<Behandlung> behandlungen = patient.getBehandlungen();
        List<Behandlung> updated = behandlungen == null ? new ArrayList<>() : new ArrayList<>(behandlungen);

        boolean alreadyLinked = updated.stream()
                .anyMatch(existing -> Objects.equals(existing.getId(), behandlung.getId()));

        if (!alreadyLinked) {
            updated.add(behandlung);
            patient.setBehandlungen(updated);
            patientRepository.save(patient);
        }
    }

    private void unlinkBehandlungFromPatient(Patient patient, String behandlungId) {
        if (patient == null || behandlungId == null) {
            return;
        }

        List<Behandlung> behandlungen = patient.getBehandlungen();
        if (behandlungen == null || behandlungen.isEmpty()) {
            return;
        }

        List<Behandlung> updated = new ArrayList<>(behandlungen);
        boolean removed = updated.removeIf(existing -> Objects.equals(existing.getId(), behandlungId));

        if (removed) {
            patient.setBehandlungen(updated);
            patientRepository.save(patient);
        }
    }

    private void linkBehandlungWithArzt(Arzt arzt, Behandlung behandlung) {
        if (arzt == null || behandlung == null) {
            return;
        }

        List<Behandlung> behandlungen = arzt.getBehandlungen();
        List<Behandlung> updated = behandlungen == null ? new ArrayList<>() : new ArrayList<>(behandlungen);

        boolean alreadyLinked = updated.stream()
                .anyMatch(existing -> Objects.equals(existing.getId(), behandlung.getId()));

        if (!alreadyLinked) {
            updated.add(behandlung);
            arzt.setBehandlungen(updated);
            arztRepository.save(arzt);
        }
    }

    private void unlinkBehandlungFromArzt(Arzt arzt, String behandlungId) {
        if (arzt == null || behandlungId == null) {
            return;
        }

        List<Behandlung> behandlungen = arzt.getBehandlungen();
        if (behandlungen == null || behandlungen.isEmpty()) {
            return;
        }

        List<Behandlung> updated = new ArrayList<>(behandlungen);
        boolean removed = updated.removeIf(existing -> Objects.equals(existing.getId(), behandlungId));

        if (removed) {
            arzt.setBehandlungen(updated);
            arztRepository.save(arzt);
        }
    }

    private String getPatientId(Patient patient) {
        return patient != null ? patient.getId() : null;
    }

    private String getArztId(Arzt arzt) {
        return arzt != null ? arzt.getId() : null;
    }
}
