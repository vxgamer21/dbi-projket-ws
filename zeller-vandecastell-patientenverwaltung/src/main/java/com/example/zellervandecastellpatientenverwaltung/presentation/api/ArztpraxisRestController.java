package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.dtos.ArztpraxisDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.service.ArztpraxisService;
import com.example.zellervandecastellpatientenverwaltung.commands.ArztpraxisCommands;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/arztpraxis")
public class ArztpraxisRestController {

    private final ArztpraxisService arztpraxisService;

    @GetMapping
    public ResponseEntity<List<ArztpraxisDto>> getAllArztpraxen() {
        return ResponseEntity.ok(arztpraxisService.getAll()
                .stream()
                .map(ArztpraxisDto::new)
                .toList());
    }

    @GetMapping("/{arztpraxisId}")
    public ResponseEntity<ArztpraxisDto> getArztpraxis(@PathVariable String arztpraxisId) {
        return arztpraxisService.getArztpraxis(arztpraxisId)
                .map(ArztpraxisDto::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Arztpraxis nicht gefunden"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArztpraxisDto createArztpraxis(@Valid @RequestBody ArztpraxisCommands.CreateArztpraxisCommand command) {
        var createdArztpraxis = arztpraxisService.createArztpraxis(
                command.name(),
                command.istKassenarzt(),
                String.valueOf(command.arztId()),
                null
        );
        return new ArztpraxisDto(createdArztpraxis);
    }
}
