package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
@Transactional(readOnly = true)
public class ArztService {
    private final ArztRepository arztRepository;

    @Transactional(readOnly = false)
    public Arzt createArzt(String name, LocalDate gebDatum, Long svnr,
                           Fachgebiet fachgebiet, Adresse adresse, TelefonNummer telefonnummer, Email email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht null oder leer sein.");
        }
        if (svnr == null) {
            throw new IllegalArgumentException("SVNR darf nicht null sein.");
        }
        if (fachgebiet == null) {
            throw new IllegalArgumentException("Fachgebiet darf nicht null sein.");
        }

        Arzt arzt = Arzt.builder()
                .name(name)
                .gebDatum(gebDatum)
                .svnr(svnr)
                .fachgebiet(fachgebiet)
                .adresse(adresse)
                .telefonNummer(telefonnummer)
                .email(email)
                .apiKey(ApiKeyGenerator.generateApiKey())
                .build();

        System.out.println("Arzt created: " + arzt);


        return arztRepository.save(arzt);
    }


    public List<Arzt> getAll() {
        return arztRepository.findAll();
    }

    public Optional<Arzt> getArzt(Long arztId) {
        return arztRepository.findById(new Arzt.ArztId(arztId))
                .or(() -> {
                    throw new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden");
                });
    }


    @Transactional(readOnly = false)
    public Arzt updateArzt(Long arztId, String name, LocalDate gebDatum, Long svnr,
                           Fachgebiet fachgebiet, Adresse adresse, TelefonNummer telefonnummer, Email email) {
        Arzt arzt = arztRepository.findById(new Arzt.ArztId(arztId))
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden"));

        if (name != null) arzt.setName(name);
        if (gebDatum != null) arzt.setGebDatum(gebDatum);
        if (svnr != null) arzt.setSvnr(svnr);
        if (fachgebiet != null) arzt.setFachgebiet(fachgebiet);
        if (adresse != null) arzt.setAdresse(adresse);
        if (telefonnummer != null) arzt.setTelefonNummer(telefonnummer);
        if (email != null) arzt.setEmail(email);

        return arztRepository.save(arzt);
    }

    @Transactional(readOnly = false)
    public void deleteArzt(Long arztId) {
        Arzt arzt = arztRepository.findById(new Arzt.ArztId(arztId))
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden"));
        arztRepository.delete(arzt);
    }

}
