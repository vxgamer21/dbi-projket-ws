package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.RechnungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
@Transactional(readOnly = true)
public class RechnungService {

    private final ArztRepository arztRepository;
    private final PatientRepository patientRepository;
    private final RechnungRepository rechnungRepository;

    @Transactional
    public Rechnung createRechnung(Long patientId, Long arztId, double betrag, LocalDateTime datum, boolean bezahlt) {
        Arzt arzt = arztRepository.findById(new Arzt.ArztId(arztId))
                .orElseThrow(() -> new IllegalArgumentException("Arzt not found"));

        Patient patient = patientRepository.findById(new Patient.PatientID(patientId))
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        String apiKey = ApiKeyGenerator.generateApiKey();

        Rechnung rechnung = Rechnung.builder()
                .betrag(betrag)
                .datum(datum)
                .bezahlt(bezahlt)
                .arzt(arzt)
                .patient(patient)
                .apiKey(apiKey)
                .build();

        return rechnungRepository.save(rechnung);
    }

    public List<Rechnung> getAll() {
        return rechnungRepository.findAll();
    }

    public Optional<Rechnung> getRechnung(Long rechnungId) {
        return rechnungRepository.findById(new Rechnung.RechnungId(rechnungId));
    }

    @Transactional
    public Rechnung updateRechnung(Long rechnungId, double neuerBetrag, boolean bezahlt) {
        Rechnung rechnung = rechnungRepository.findById(new Rechnung.RechnungId(rechnungId))
                .orElseThrow(() -> new IllegalArgumentException("Rechnung mit ID " + rechnungId + " nicht gefunden."));

        rechnung.setBetrag(neuerBetrag);
        rechnung.setBezahlt(bezahlt);

        return rechnungRepository.save(rechnung);
    }

    @Transactional
    public void deleteRechnung(Long rechnungId) {
        if (!rechnungRepository.existsById(new Rechnung.RechnungId(rechnungId))) {
            throw new IllegalArgumentException("Rechnung mit ID " + rechnungId + " existiert nicht.");
        }
        rechnungRepository.deleteById(new Rechnung.RechnungId(rechnungId));
    }
}
