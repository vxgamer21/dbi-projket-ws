package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.dtos.WarteraumDto;
import com.example.zellervandecastellpatientenverwaltung.service.WarteRaumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/warteraum")
public class WarteRaumRestController {

    private final WarteRaumService warteRaumService;

    @GetMapping
    public ResponseEntity<List<WarteraumDto>> getAllWarteraeume() {
        return ResponseEntity.ok(
                warteRaumService.getAll()
                        .stream()
                        .map(WarteraumDto::new)
                        .toList()
        );
    }

    @GetMapping("/{warteraumId}")
    public ResponseEntity<WarteraumDto> getWarteraum(@PathVariable Long warteraumId) {
        return warteRaumService.getWarteraum(warteraumId)
                .map(WarteraumDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WarteraumDto> createWarteraum(@RequestParam int anzahlSitzplaetze) {
        var warteraum = warteRaumService.createWarteraum(anzahlSitzplaetze);
        return ResponseEntity.ok(new WarteraumDto(warteraum));
    }

    @PutMapping("/{warteraumId}")
    public ResponseEntity<WarteraumDto> updateWarteraum(
            @PathVariable Long warteraumId,
            @RequestParam int neueAnzahlSitzplaetze
    ) {
        var updatedWarteraum = warteRaumService.updateWarteraum(warteraumId, neueAnzahlSitzplaetze);
        return ResponseEntity.ok(new WarteraumDto(updatedWarteraum));
    }

    @DeleteMapping("/{warteraumId}")
    public ResponseEntity<Void> deleteWarteraum(@PathVariable Long warteraumId) {
        warteRaumService.deleteWarteraum(warteraumId);
        return ResponseEntity.noContent().build();
    }
}
