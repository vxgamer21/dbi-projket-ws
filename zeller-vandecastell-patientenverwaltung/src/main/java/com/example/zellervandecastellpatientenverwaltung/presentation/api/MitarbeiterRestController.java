package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.dtos.MitarbeiterDto;
import com.example.zellervandecastellpatientenverwaltung.service.MitarbeiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterRestController {

    private final MitarbeiterService mitarbeiterService;

    @GetMapping
    public ResponseEntity<List<MitarbeiterDto>> getAllMitarbeiter() {
        return ResponseEntity.ok(mitarbeiterService.getAll()
                .stream()
                .map(MitarbeiterDto::new)
                .toList());
    }

    @GetMapping("/{mitarbeiterId}")
    public ResponseEntity<MitarbeiterDto> getMitarbeiter(@PathVariable String mitarbeiterId) {
        return mitarbeiterService.getMitarbeiter(mitarbeiterId)
                .map(MitarbeiterDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MitarbeiterDto> createMitarbeiter(
            @RequestParam String name,
            @RequestParam LocalDate gebDatum,
            @RequestParam Long svnr,
            @RequestParam Long gehalt) {

        var mitarbeiter = mitarbeiterService.createMitarbeiter(name, gebDatum, svnr, gehalt);
        return ResponseEntity.ok(new MitarbeiterDto(mitarbeiter));
    }

    @PutMapping("/{mitarbeiterId}")
    public ResponseEntity<MitarbeiterDto> updateMitarbeiter(
            @PathVariable String mitarbeiterId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate gebDatum,
            @RequestParam(required = false) Long svnr,
            @RequestParam(required = false) Long gehalt) {

        var mitarbeiter = mitarbeiterService.updateMitarbeiter(mitarbeiterId, name, gebDatum, svnr, gehalt);
        return ResponseEntity.ok(new MitarbeiterDto(mitarbeiter));
    }

    @DeleteMapping("/{mitarbeiterId}")
    public ResponseEntity<Void> deleteMitarbeiter(@PathVariable String mitarbeiterId) {
        mitarbeiterService.deleteMitarbeiter(mitarbeiterId);
        return ResponseEntity.noContent().build();
    }
}
