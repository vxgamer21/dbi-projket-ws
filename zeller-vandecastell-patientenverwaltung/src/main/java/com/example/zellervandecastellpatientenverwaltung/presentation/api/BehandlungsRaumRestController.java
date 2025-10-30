package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungsRaumDto;
import com.example.zellervandecastellpatientenverwaltung.service.BehandlungsRaumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/behandlungsraum")
public class BehandlungsRaumRestController {

    private final BehandlungsRaumService behandlungsRaumService;

    @GetMapping
    public ResponseEntity<List<BehandlungsRaumDto>> getAllBehandlungsraeume() {
        return ResponseEntity.ok(behandlungsRaumService.getAll()
                .stream()
                .map(BehandlungsRaumDto::new)
                .toList());
    }

    @GetMapping("/{behandlungsraumId}")
    public ResponseEntity<BehandlungsRaumDto> getBehandlungsraum(@PathVariable Long behandlungsraumId) {
        return behandlungsRaumService.getBehandlungsraum(behandlungsraumId)
                .map(BehandlungsRaumDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BehandlungsRaumDto> createBehandlungsraum(@RequestParam Long behandlungId,
                                                                    @RequestParam String ausstattung,
                                                                    @RequestParam boolean isFrei) {
        var behandlungsRaum = behandlungsRaumService.createBehandlungsraum(behandlungId, ausstattung, isFrei);
        return ResponseEntity.status(201).body(new BehandlungsRaumDto(behandlungsRaum));
    }

    @PutMapping("/{behandlungsraumId}")
    public ResponseEntity<BehandlungsRaumDto> updateBehandlungsraum(@PathVariable Long behandlungsraumId,
                                                                    @RequestParam(required = false) String ausstattung,
                                                                    @RequestParam(required = false) Boolean isFrei) {
        var updatedBehandlungsRaum = behandlungsRaumService.updateBehandlungsraum(behandlungsraumId, ausstattung, isFrei);
        return ResponseEntity.ok(new BehandlungsRaumDto(updatedBehandlungsRaum));
    }

    @DeleteMapping("/{behandlungsraumId}")
    public ResponseEntity<Void> deleteBehandlungsraum(@PathVariable Long behandlungsraumId) {
        behandlungsRaumService.deleteBehandlungsraum(behandlungsraumId);
        return ResponseEntity.noContent().build();
    }
}
