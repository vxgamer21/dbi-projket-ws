package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

        var behandlung = Behandlung.builder()
                .beginn(beginn)
                .ende(ende)
                .diagnose(diagnose)
                .arzt(arzt)
                .patient(patient)
                .apiKey(apiKey)
                .build();

        return behandlungRepository.save(behandlung);
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

        return behandlungRepository.save(behandlung);
    }

    @Transactional
    public void deleteBehandlung(String behandlungId) {
        Behandlung behandlung = behandlungRepository.findById(behandlungId)
                .orElseThrow(() -> new IllegalArgumentException("Behandlung not found"));
        behandlungRepository.delete(behandlung);
    }
}
