package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.commands.ArztCommands;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.service.ArztService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/arzt")
public class ArztRestController {

    private static final Logger logger = LoggerFactory.getLogger(ArztRestController.class);
    private final ArztService arztService;

    @GetMapping
    public ResponseEntity<List<ArztDto>> getAllAerzte() {
        logger.info("GET /arzt aufgerufen - Alle Aerzte werden abgerufen");
        return ResponseEntity.ok(arztService.getAll()
                .stream()
                .map(ArztDto::new)
                .toList());
    }

    @GetMapping("/{arztId}")
    public ResponseEntity<ArztDto> getArzt(@PathVariable String arztId) {
        logger.info("GET /arzt/" + arztId + " aufgerufen - Arzt mit ID " + arztId + " wird abgerufen");
        return arztService.getArzt(arztId)
                .map(ArztDto::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + arztId + " nicht gefunden"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArztDto createArzt(@RequestBody ArztCommands.CreateArztCommand command) {
        var createdArzt = arztService.createArzt(
                command.name(),
                command.geburtsdatum(),
                command.svnr(),
                command.fachgebiet(),
                command.adresse(),
                command.telefonnummer(),
                command.email()
        );
        logger.info("POST /arzt aufgerufen - Neuer Arzt erstellt: " + createdArzt.getName());
        return new ArztDto(createdArzt);
    }

    @PutMapping("/{arztId}")
    public ResponseEntity<ArztDto> updateArzt(@PathVariable String arztId,
                                              @RequestBody ArztCommands.CreateArztCommand command) {
        var updatedArzt = arztService.updateArzt(
                arztId,
                command.name(),
                command.geburtsdatum(),
                command.svnr(),
                command.fachgebiet(),
                command.adresse(),
                command.telefonnummer(),
                command.email()
        );
        logger.info("PUT /arzt/" + arztId + " aufgerufen - Arzt mit ID " + arztId + " aktualisiert");
        return ResponseEntity.ok(new ArztDto(updatedArzt));
    }

    @DeleteMapping("/{arztId}")
    public ResponseEntity<Void> deleteArzt(@PathVariable String arztId) {
        arztService.deleteArzt(arztId);
        logger.info("DELETE /arzt/" + arztId + " aufgerufen - Arzt mit ID " + arztId + " gelöscht");
        return ResponseEntity.noContent().build();
    }
}
