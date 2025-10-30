package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import com.example.zellervandecastellpatientenverwaltung.dtos.PatientDto;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patient")
public class PatientRestController {

    private final PatientService patientService;
    private static final Logger logger = LoggerFactory.getLogger(PatientRestController.class);

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatienten() {
        logger.info("GET /patient aufgerufen - Alle Patienten werden abgerufen");
        return ResponseEntity.ok(
                patientService.getAll()
                        .stream()
                        .map(PatientDto::new)
                        .toList()
        );
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable String patientId) {
        logger.info("GET /patient/{} aufgerufen - Patient mit ID {} wird abgerufen", patientId, patientId);
        return patientService.getPatient(patientId)
                .map(PatientDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(
            @RequestParam String name,
            @RequestParam LocalDate geburtsdatum,
            @RequestParam Long svnr,
            @RequestParam Versicherungsart versicherungsart,
            @RequestBody Adresse adresse,
            @RequestBody TelefonNummer telefonnummer
    ) {
        var patient = patientService.createPatient(name, geburtsdatum, svnr, versicherungsart, adresse, telefonnummer);
        logger.info("POST /patient aufgerufen - Neuer Patient erstellt: {}", patient.getName());
        return ResponseEntity.ok(new PatientDto(patient));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable String patientId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate geburtsdatum,
            @RequestParam(required = false) Long svnr,
            @RequestParam(required = false) Versicherungsart versicherungsart,
            @RequestBody(required = false) Adresse adresse,
            @RequestBody(required = false) TelefonNummer telefonnummer
    ) {
        var updatedPatient = patientService.updatePatient(
                patientId, name, geburtsdatum, svnr, versicherungsart, adresse, telefonnummer
        );
        logger.info("PUT /patient/{} aufgerufen - Patient mit ID {} aktualisiert", patientId, patientId);
        return ResponseEntity.ok(new PatientDto(updatedPatient));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable String patientId) {
        patientService.deletePatient(patientId);
        logger.info("DELETE /patient/{} aufgerufen - Patient mit ID {} gelöscht", patientId, patientId);
        return ResponseEntity.noContent().build();
    }
}
