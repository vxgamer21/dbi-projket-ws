package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.commands.BehandlungCommands;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.service.BehandlungService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/behandlung")
public class BehandlungRestController {

    private final BehandlungService behandlungService;
    private static final Logger logger = LoggerFactory.getLogger(BehandlungRestController.class);


    @GetMapping
    public ResponseEntity<List<BehandlungDto>> getAllBehandlungen() {
        logger.info("GET /behandlung aufgerufen - Alle Behandlungen werden abgerufen");
        return ResponseEntity.ok(behandlungService.getAll()
                .stream()
                .map(BehandlungDto::new)
                .toList());
    }

    @GetMapping("/{behandlungId}")
    public ResponseEntity<BehandlungDto> getBehandlung(@PathVariable Long behandlungId) {
        logger.info("GET /behandlung/{} aufgerufen - Behandlung mit ID {} wird abgerufen", behandlungId, behandlungId);
        return behandlungService.getBehandlung(behandlungId)
                .map(BehandlungDto::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Behandlung mit ID " + behandlungId + " nicht gefunden"));
    }

    @PostMapping
    public ResponseEntity<BehandlungDto> createBehandlung(
            @RequestParam LocalDateTime beginn,
            @RequestParam LocalDateTime ende,
            @RequestParam String diagnose,
            @RequestParam Long arztId,
            @RequestParam Long patientId) {

        var behandlung = behandlungService.createBehandlung(beginn, ende, diagnose, arztId, patientId);
        logger.info("POST /behandlung aufgerufen - Neue Behandlung erstellt: {}", behandlung.getId());
        return ResponseEntity.ok(new BehandlungDto(behandlung));
    }

    @PutMapping("/{behandlungId}")
    public ResponseEntity<BehandlungDto> updateBehandlung(
            @PathVariable Long behandlungId,
            @RequestParam(required = false) LocalDateTime beginn,
            @RequestParam(required = false) LocalDateTime ende,
            @RequestParam(required = false) String diagnose,
            @RequestParam(required = false) Long arztId,
            @RequestParam(required = false) Long patientId) {

        var behandlung = behandlungService.updateBehandlung(behandlungId, beginn, ende, diagnose, arztId, patientId);
        logger.info("PUT /behandlung/{} aufgerufen - Behandlung aktualisiert: {}", behandlungId, behandlung.getId());
        return ResponseEntity.ok(new BehandlungDto(behandlung));
    }

    @DeleteMapping("/{behandlungId}")
    public ResponseEntity<Void> deleteBehandlung(@PathVariable Long behandlungId) {
        behandlungService.deleteBehandlung(behandlungId);
        logger.info("DELETE /behandlung/{} aufgerufen - Behandlung mit ID {} wurde gelöscht", behandlungId, behandlungId);
        return ResponseEntity.noContent().build();
    }
}
