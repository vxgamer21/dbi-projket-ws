package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.dtos.RechnungDto;
import com.example.zellervandecastellpatientenverwaltung.service.RechnungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rechnung")
public class RechnungRestController {

    private final RechnungService rechnungService;

    @GetMapping
    public ResponseEntity<List<RechnungDto>> getAllRechnungen() {
        return ResponseEntity.ok(
                rechnungService.getAll()
                        .stream()
                        .map(RechnungDto::new)
                        .toList()
        );
    }

    @GetMapping("/{rechnungId}")
    public ResponseEntity<RechnungDto> getRechnung(@PathVariable String rechnungId) {
        return rechnungService.getRechnung(rechnungId)
                .map(RechnungDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RechnungDto> createRechnung(
            @RequestParam String patientId,
            @RequestParam String arztId,
            @RequestParam double betrag,
            @RequestParam String datum,   // ISO 8601 String erwartet
            @RequestParam boolean bezahlt
    ) {
        LocalDateTime parsedDatum = LocalDateTime.parse(datum);
        var rechnung = rechnungService.createRechnung(patientId, arztId, betrag, parsedDatum, bezahlt);
        return ResponseEntity.ok(new RechnungDto(rechnung));
    }

    @PutMapping("/{rechnungId}")
    public ResponseEntity<RechnungDto> updateRechnung(
            @PathVariable String rechnungId,
            @RequestParam double neuerBetrag,
            @RequestParam boolean bezahlt
    ) {
        var updatedRechnung = rechnungService.updateRechnung(rechnungId, neuerBetrag, bezahlt);
        return ResponseEntity.ok(new RechnungDto(updatedRechnung));
    }

    @DeleteMapping("/{rechnungId}")
    public ResponseEntity<Void> deleteRechnung(@PathVariable String rechnungId) {
        rechnungService.deleteRechnung(rechnungId);
        return ResponseEntity.noContent().build();
    }
}
