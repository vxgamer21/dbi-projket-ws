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

    @Transactional(readOnly = false)
    public Behandlungsraum createBehandlungsraum(Long behandlungId, String ausstattung, boolean isFrei) {

        if (ausstattung == null || ausstattung.isEmpty()) {
            throw new IllegalArgumentException("Ausstattung darf nicht null oder leer sein.");
        }

        Behandlung behandlung = behandlungRepository.findById(new Behandlung.BehandlungId(behandlungId))
                .orElseThrow(() -> new IllegalArgumentException("Behandlung not found"));


        var behandlungsRaum = FixturesFactory.BehandlungsRaum1Frei();

        return behandlungsRaumRepository.save(behandlungsRaum);
    }

    public List<Behandlungsraum> getAll() {
        return behandlungsRaumRepository.findAll();
    }

    public Optional<Behandlungsraum> getBehandlungsraum(Long behandlungsRaumId) {
        return behandlungsRaumRepository.findById(new Behandlungsraum.BehandlungsraumId(behandlungsRaumId));
    }

    @Transactional(readOnly = false)
    public Behandlungsraum updateBehandlungsraum(Long behandlungsRaumId, String ausstattung, Boolean isFrei) {
        Behandlungsraum behandlungsRaum = behandlungsRaumRepository.findById(new Behandlungsraum.BehandlungsraumId(behandlungsRaumId))
                .orElseThrow(() -> new IllegalArgumentException("Behandlungsraum nicht gefunden"));

        if (ausstattung != null && !ausstattung.trim().isEmpty()) {
            behandlungsRaum.setAusstattung(ausstattung);
        }
        if (isFrei != null) {
            behandlungsRaum.setIsFrei(isFrei);
        }

        return behandlungsRaumRepository.save(behandlungsRaum);
    }

    @Transactional(readOnly = false)
    public void deleteBehandlungsraum(Long behandlungsRaumId) {
        if (!behandlungsRaumRepository.existsById(new Behandlungsraum.BehandlungsraumId(behandlungsRaumId))) {
            throw new IllegalArgumentException("Behandlungsraum existiert nicht");
        }
        behandlungsRaumRepository.deleteById(new Behandlungsraum.BehandlungsraumId(behandlungsRaumId));
    }
}
