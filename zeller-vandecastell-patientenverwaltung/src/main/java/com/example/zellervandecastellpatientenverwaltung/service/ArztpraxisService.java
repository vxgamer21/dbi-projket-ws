package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.ArztpraxisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
@Transactional(readOnly = true)
public class ArztpraxisService {

    private final ArztRepository arztRepository;
    private final ArztpraxisRepository arztpraxisRepository;

    @Transactional
    public Arztpraxis createArztpraxis(String name, boolean istKassenarzt, String arztId, String apiKey) {
        Arzt arzt = arztRepository.findById(arztId)
                .orElseThrow(() -> new IllegalArgumentException("Arzt not found"));

        var telefonNummer = FixturesFactory.StandardMobil();
        var adresse = FixturesFactory.Ringstrasse();

        if (apiKey == null || apiKey.isBlank()) {
            apiKey = ApiKeyGenerator.generateApiKey();
        }

        var arztpraxis = Arztpraxis.builder()
                .name(name)
                .istKassenarzt(istKassenarzt)
                .aerzte(List.of(arzt))
                .telefonNummer(telefonNummer)
                .adresse(adresse)
                .apiKey(apiKey)
                .build();

        return arztpraxisRepository.save(arztpraxis);
    }

    public List<Arztpraxis> getAll() {
        return arztpraxisRepository.findAll();
    }

    public Optional<Arztpraxis> getArztpraxis(String arztpraxisId) {
        return arztpraxisRepository.findById(arztpraxisId);
    }
}
