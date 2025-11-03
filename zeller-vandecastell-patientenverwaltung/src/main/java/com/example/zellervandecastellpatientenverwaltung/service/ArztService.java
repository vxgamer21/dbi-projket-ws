package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
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
public class ArztService {

    private final ArztRepository arztRepository;

    @Transactional
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

    public Optional<Arzt> getArzt(String arztId) {
        return arztRepository.findById(arztId)
                .or(() -> {
                    throw new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden");
                });
    }

    @Transactional
    public Arzt updateArzt(String arztId, String name, LocalDate gebDatum, Long svnr,
                           Fachgebiet fachgebiet, Adresse adresse, TelefonNummer telefonnummer, Email email) {
        Arzt arzt = arztRepository.findById(arztId)
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

    @Transactional
    public void deleteArzt(String arztId) {
        Arzt arzt = arztRepository.findById(arztId)
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden"));
        arztRepository.delete(arzt);
    }
}
