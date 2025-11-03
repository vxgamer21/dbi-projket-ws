package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Mitarbeiter;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.persistence.MitarbeiterRepository;
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
public class MitarbeiterService {

    private final MitarbeiterRepository mitarbeiterRepository;

    @Transactional
    public Mitarbeiter createMitarbeiter(String name, LocalDate gebDatum, Long svnr, Long gehalt) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht null oder leer sein.");
        }
        if (svnr == null) {
            throw new IllegalArgumentException("SVNR darf nicht null sein.");
        }
        if (gehalt == null || gehalt < 0) {
            throw new IllegalArgumentException("Es gibt kein negatives Gehalt.");
        }
        if (gehalt > 100000) {
            throw new IllegalArgumentException("Das Gehalt darf nicht höher als 100000 sein.");
        }

        String apiKey = ApiKeyGenerator.generateApiKey();

        Mitarbeiter mitarbeiter = Mitarbeiter.builder()
                .name(name)
                .gebDatum(gebDatum)
                .svnr(svnr)
                .gehalt(gehalt)
                .email(new Email("max@muster.at"))
                .apiKey(apiKey)
                .build();

        return mitarbeiterRepository.save(mitarbeiter);
    }

    public List<Mitarbeiter> getAll() {
        return mitarbeiterRepository.findAll();
    }

    public Optional<Mitarbeiter> getMitarbeiter(String mitarbeiterId) {
        return mitarbeiterRepository.findById(mitarbeiterId);
    }

    @Transactional
    public Mitarbeiter updateMitarbeiter(String mitarbeiterId, String neuerName, LocalDate neuesGebDatum, Long neueSvnr, Long neuesGehalt) {
        Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(mitarbeiterId)
                .orElseThrow(() -> new IllegalArgumentException("Mitarbeiter mit ID " + mitarbeiterId + " nicht gefunden."));

        if (neuerName != null && !neuerName.trim().isEmpty()) {
            mitarbeiter.setName(neuerName);
        }
        if (neuesGebDatum != null) {
            mitarbeiter.setGebDatum(neuesGebDatum);
        }
        if (neueSvnr != null) {
            mitarbeiter.setSvnr(neueSvnr);
        }
        if (neuesGehalt != null) {
            if (neuesGehalt < 0) {
                throw new IllegalArgumentException("Gehalt darf nicht negativ sein.");
            }
            if (neuesGehalt > 100000) {
                throw new IllegalArgumentException("Das Gehalt darf nicht höher als 100000 sein.");
            }
            mitarbeiter.setGehalt(neuesGehalt);
        }

        return mitarbeiterRepository.save(mitarbeiter);
    }

    @Transactional
    public void deleteMitarbeiter(String mitarbeiterId) {
        if (!mitarbeiterRepository.existsById(mitarbeiterId)) {
            throw new IllegalArgumentException("Mitarbeiter mit ID " + mitarbeiterId + " existiert nicht.");
        }
        mitarbeiterRepository.deleteById(mitarbeiterId);
    }
}
