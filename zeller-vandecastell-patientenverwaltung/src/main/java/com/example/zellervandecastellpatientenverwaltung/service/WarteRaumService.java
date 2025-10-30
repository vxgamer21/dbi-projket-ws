package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import com.example.zellervandecastellpatientenverwaltung.persistence.WarteraumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WarteRaumService {

    private final WarteraumRepository warteraumRepository;

    @Transactional
    public Warteraum createWarteraum(int anzahlSitzplaetze) {
        if (anzahlSitzplaetze < 0) {
            throw new IllegalArgumentException("Anzahl Sitzplätze muss einen gültigen Wert haben!");
        }

        var warteRaum = FixturesFactory.WarteRaum22Seats();
        warteRaum.setAnzahlSitzplaetze(anzahlSitzplaetze);

        return warteraumRepository.save(warteRaum);
    }

    public List<Warteraum> getAll() {
        return warteraumRepository.findAll();
    }

    public Optional<Warteraum> getWarteraum(String warteraumId) {
        return warteraumRepository.findById(warteraumId);
    }

    @Transactional
    public Warteraum updateWarteraum(String warteraumId, int neueAnzahlSitzplaetze) {
        Warteraum warteraum = warteraumRepository.findById(warteraumId)
                .orElseThrow(() -> new IllegalArgumentException("Warteraum mit ID " + warteraumId + " nicht gefunden."));

        if (neueAnzahlSitzplaetze < 0) {
            throw new IllegalArgumentException("Anzahl der Sitzplätze muss positiv sein!");
        }

        warteraum.setAnzahlSitzplaetze(neueAnzahlSitzplaetze);
        return warteraumRepository.save(warteraum);
    }

    @Transactional
    public void deleteWarteraum(String warteraumId) {
        if (!warteraumRepository.existsById(warteraumId)) {
            throw new IllegalArgumentException("Warteraum mit ID " + warteraumId + " existiert nicht.");
        }
        warteraumRepository.deleteById(warteraumId);
    }
}
