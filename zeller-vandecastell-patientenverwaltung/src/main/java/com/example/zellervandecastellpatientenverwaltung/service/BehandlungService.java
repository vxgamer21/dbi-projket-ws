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
    private final EmbeddedDocumentMapper embeddedDocumentMapper;

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
                .arzt(embeddedDocumentMapper.toEmbeddedArzt(arzt))
                .patient(embeddedDocumentMapper.toEmbeddedPatient(patient))
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

        String previousPatientId = getPatientId(behandlung.getPatient());
        String previousArztId = getArztId(behandlung.getArzt());

        Patient targetPatient = patientId != null
                ? patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"))
                : findPatientById(previousPatientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Arzt targetArzt = arztId != null
                ? arztRepository.findById(arztId)
                .orElseThrow(() -> new IllegalArgumentException("Arzt not found"))
                : findArztById(previousArztId)
                .orElseThrow(() -> new IllegalArgumentException("Arzt not found"));

        if (beginn != null) behandlung.setBeginn(beginn);
        if (ende != null) behandlung.setEnde(ende);
        if (diagnose != null) behandlung.setDiagnose(diagnose);

        behandlung.setArzt(embeddedDocumentMapper.toEmbeddedArzt(targetArzt));
        behandlung.setPatient(embeddedDocumentMapper.toEmbeddedPatient(targetPatient));

        Behandlung saved = behandlungRepository.save(behandlung);

        if (!Objects.equals(previousPatientId, targetPatient.getId())) {
            findPatientById(previousPatientId)
                    .ifPresent(patient -> unlinkBehandlungFromPatient(patient, saved.getId()));
        }
        linkBehandlungWithPatient(targetPatient, saved);

        if (!Objects.equals(previousArztId, targetArzt.getId())) {
            findArztById(previousArztId)
                    .ifPresent(arzt -> unlinkBehandlungFromArzt(arzt, saved.getId()));
        }
        linkBehandlungWithArzt(targetArzt, saved);

        return saved;
    }

    @Transactional
    public void deleteBehandlung(String behandlungId) {
        Behandlung behandlung = behandlungRepository.findById(behandlungId)
                .orElseThrow(() -> new IllegalArgumentException("Behandlung not found"));
        behandlungRepository.delete(behandlung);

        findPatientById(getPatientId(behandlung.getPatient()))
                .ifPresent(patient -> unlinkBehandlungFromPatient(patient, behandlung.getId()));
        findArztById(getArztId(behandlung.getArzt()))
                .ifPresent(arzt -> unlinkBehandlungFromArzt(arzt, behandlung.getId()));
    }

    private void linkBehandlungWithPatient(Patient patient, Behandlung behandlung) {
        if (patient == null || behandlung == null) {
            return;
        }

        List<Behandlung> behandlungen = patient.getBehandlungen();
        List<Behandlung> updated = behandlungen == null ? new ArrayList<>() : new ArrayList<>(behandlungen);

        Behandlung embedded = embeddedDocumentMapper.toEmbeddedBehandlung(behandlung);
        if (embedded != null) {
            embedded.setPatient(embeddedDocumentMapper.toEmbeddedPatient(patient));
        }

        boolean replaced = false;
        for (int i = 0; i < updated.size(); i++) {
            Behandlung existing = updated.get(i);
            if (Objects.equals(existing.getId(), embedded.getId())) {
                updated.set(i, embedded);
                replaced = true;
                break;
            }
        }

        if (!replaced) {
            updated.add(embedded);
        }

        patient.setBehandlungen(updated);
        patientRepository.save(patient);
    }

    private void unlinkBehandlungFromPatient(Patient patient, String behandlungId) {
        if (patient == null || behandlungId == null) {
            return;
        }

        List<Behandlung> behandlungen = patient.getBehandlungen();
        if (behandlungen == null || behandlungen.isEmpty()) {
            return;
        }

        List<Behandlung> updated = new ArrayList<>();
        boolean removed = false;
        for (Behandlung existing : behandlungen) {
            if (Objects.equals(existing.getId(), behandlungId)) {
                removed = true;
                continue;
            }
            updated.add(existing);
        }

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

        Behandlung embedded = embeddedDocumentMapper.toEmbeddedBehandlung(behandlung);
        if (embedded != null) {
            embedded.setArzt(embeddedDocumentMapper.toEmbeddedArzt(arzt));
        }

        boolean replaced = false;
        for (int i = 0; i < updated.size(); i++) {
            Behandlung existing = updated.get(i);
            if (Objects.equals(existing.getId(), embedded.getId())) {
                updated.set(i, embedded);
                replaced = true;
                break;
            }
        }

        if (!replaced) {
            updated.add(embedded);
        }

        arzt.setBehandlungen(updated);
        arztRepository.save(arzt);
    }

    private void unlinkBehandlungFromArzt(Arzt arzt, String behandlungId) {
        if (arzt == null || behandlungId == null) {
            return;
        }

        List<Behandlung> behandlungen = arzt.getBehandlungen();
        if (behandlungen == null || behandlungen.isEmpty()) {
            return;
        }

        List<Behandlung> updated = new ArrayList<>();
        boolean removed = false;
        for (Behandlung existing : behandlungen) {
            if (Objects.equals(existing.getId(), behandlungId)) {
                removed = true;
                continue;
            }
            updated.add(existing);
        }

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

    private Optional<Patient> findPatientById(String patientId) {
        if (patientId == null) {
            return Optional.empty();
        }
        return patientRepository.findById(patientId);
    }

    private Optional<Arzt> findArztById(String arztId) {
        if (arztId == null) {
            return Optional.empty();
        }
        return arztRepository.findById(arztId);
    }
}
