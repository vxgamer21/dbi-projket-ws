package com.example.zellervandecastellpatientenverwaltung.service;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import com.example.zellervandecastellpatientenverwaltung.domain.Behandlungsraum;
import com.example.zellervandecastellpatientenverwaltung.domain.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungRepository;
import com.example.zellervandecastellpatientenverwaltung.persistence.BehandlungsraumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
@Transactional(readOnly = true)
public class BehandlungsRaumService {

    private final BehandlungRepository behandlungRepository;
    private final BehandlungsraumRepository behandlungsRaumRepository;

    @Transactional
    public Behandlungsraum createBehandlungsraum(String behandlungId, String ausstattung, boolean isFrei) {
        if (ausstattung == null || ausstattung.isBlank()) {
            throw new IllegalArgumentException("Ausstattung darf nicht null oder leer sein.");
        }

        Behandlung behandlung = behandlungRepository.findById(behandlungId)
                .orElseThrow(() -> new IllegalArgumentException("Behandlung not found"));

        var behandlungsRaum = FixturesFactory.BehandlungsRaum1Frei();
        behandlungsRaum.setAusstattung(ausstattung);
        behandlungsRaum.setIsFrei(isFrei);
        behandlungsRaum.setBehandlungen(List.of(behandlung));

        return behandlungsRaumRepository.save(behandlungsRaum);
    }

    public List<Behandlungsraum> getAll() {
        return behandlungsRaumRepository.findAll();
    }

    public Optional<Behandlungsraum> getBehandlungsraum(String behandlungsRaumId) {
        return behandlungsRaumRepository.findById(behandlungsRaumId);
    }

    @Transactional
    public Behandlungsraum updateBehandlungsraum(String behandlungsRaumId, String ausstattung, Boolean isFrei) {
        Behandlungsraum behandlungsRaum = behandlungsRaumRepository.findById(behandlungsRaumId)
                .orElseThrow(() -> new IllegalArgumentException("Behandlungsraum nicht gefunden"));

        if (ausstattung != null && !ausstattung.trim().isEmpty()) {
            behandlungsRaum.setAusstattung(ausstattung);
        }
        if (isFrei != null) {
            behandlungsRaum.setIsFrei(isFrei);
        }

        return behandlungsRaumRepository.save(behandlungsRaum);
    }

    @Transactional
    public void deleteBehandlungsraum(String behandlungsRaumId) {
        if (!behandlungsRaumRepository.existsById(behandlungsRaumId)) {
            throw new IllegalArgumentException("Behandlungsraum existiert nicht");
        }
        behandlungsRaumRepository.deleteById(behandlungsRaumId);
    }
}
