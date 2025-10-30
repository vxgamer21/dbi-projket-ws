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

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
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

        return warteraumRepository.save(warteRaum);
    }

    public List<Warteraum> getAll() {
        return warteraumRepository.findAll();
    }

    public Optional<Warteraum> getWarteraum(Long warteraumId) {
        return warteraumRepository.findById(new Warteraum.WarteraumId(warteraumId));
    }

    @Transactional
    public Warteraum updateWarteraum(Long warteraumId, int neueAnzahlSitzplaetze) {
        Warteraum warteraum = warteraumRepository.findById(new Warteraum.WarteraumId(warteraumId))
                .orElseThrow(() -> new IllegalArgumentException("Warteraum mit ID " + warteraumId + " nicht gefunden."));

        if (neueAnzahlSitzplaetze < 0) {
            throw new IllegalArgumentException("Anzahl der Sitzplätze muss positiv sein!");
        }

        warteraum.setAnzahlSitzplaetze(neueAnzahlSitzplaetze);
        return warteraumRepository.save(warteraum);
    }

    @Transactional
    public void deleteWarteraum(Long warteraumId) {
        if (!warteraumRepository.existsById(new Warteraum.WarteraumId(warteraumId))) {
            throw new IllegalArgumentException("Warteraum mit ID " + warteraumId + " existiert nicht.");
        }
        warteraumRepository.deleteById(new Warteraum.WarteraumId(warteraumId));
    }
}
