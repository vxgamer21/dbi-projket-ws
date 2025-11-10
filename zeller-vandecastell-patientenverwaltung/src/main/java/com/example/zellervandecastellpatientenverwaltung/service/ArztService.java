package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
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
public class ArztService {

    private final ArztRepository arztRepository;
    private final BehandlungRepository behandlungRepository;
    private final EmbeddedDocumentMapper embeddedDocumentMapper;

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
        return arztRepository.findAll().stream()
                .map(this::prepareEmbeddedArzt)
                .toList();
    }

    public Optional<Arzt> getArzt(String arztId) {
        return arztRepository.findById(arztId)
                .map(this::prepareEmbeddedArzt)
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

        Arzt updatedArzt = arztRepository.save(arzt);
        return prepareEmbeddedArzt(updatedArzt);
    }

    @Transactional
    public void deleteArzt(String arztId) {
        Arzt arzt = arztRepository.findById(arztId)
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden"));
        arztRepository.delete(arzt);
    }

    private Arzt prepareEmbeddedArzt(Arzt arzt) {
        if (arzt == null) {
            return null;
        }

        List<Behandlung> behandlungen = arzt.getBehandlungen();
        if (behandlungen == null || behandlungen.isEmpty()) {
            arzt.setBehandlungen(new ArrayList<>());
            return arzt;
        }

        Arzt embeddedSelf = embeddedDocumentMapper.toEmbeddedArzt(arzt);

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
                embedded.setArzt(embeddedSelf);
                sanitized.add(embedded);
            }
        }

        arzt.setBehandlungen(sanitized);
        return arzt;
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
