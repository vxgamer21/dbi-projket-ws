package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztFormDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.service.ArztService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/www/aerzte")
public class ArztController {

    private final ArztService arztService;

    @GetMapping
    public String getAerzte(Model model) {
        model.addAttribute("aerzte", arztService.getAll());
        return "aerzte/index";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        Arzt arzt = new Arzt();
        arzt.setEmail(new Email());
        arzt.setAdresse(new Adresse());
        arzt.setTelefonNummer(new TelefonNummer());

        model.addAttribute("arzt", arzt);
        return "aerzte/form";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("arzt") Arzt arzt, BindingResult result, Model model) {

        if (arzt.getEmail() == null) arzt.setEmail(new Email());
        if (arzt.getAdresse() == null) arzt.setAdresse(new Adresse());
        if (arzt.getTelefonNummer() == null) arzt.setTelefonNummer(new TelefonNummer());

        if (arzt.getApiKey() == null || arzt.getApiKey().trim().isEmpty()) {
            arzt.setApiKey(ApiKeyGenerator.generateApiKey());
            System.out.println("Generated API Key: " + arzt.getApiKey());
        }

        System.out.println("=== DEBUG: Formular empfangen ===");
        System.out.println("Name: " + arzt.getName());
        System.out.println("Email: " + (arzt.getEmail() != null ? arzt.getEmail().getMail() : "null"));
        System.out.println("SVNR: " + arzt.getSvnr());
        System.out.println("Geburtsdatum: " + arzt.getGebDatum());
        System.out.println("Fachgebiet: " + arzt.getFachgebiet());
        System.out.println("Adresse: " + (arzt.getAdresse() != null ? arzt.getAdresse().toString() : "null"));
        System.out.println("TelefonNummer: " + (arzt.getTelefonNummer() != null ? arzt.getTelefonNummer().toString() : "null"));

        if (result.hasErrors()) {
            System.out.println("=== VALIDATION ERRORS ===");
            result.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
                if (error instanceof org.springframework.validation.FieldError fieldError) {
                    System.out.println("Field: " + fieldError.getField() + " - Value: " + fieldError.getRejectedValue());
                }
            });
            return "aerzte/form";
        }

        try {
            Arzt savedArzt = arztService.createArzt(
                    arzt.getName(),
                    arzt.getGebDatum(),
                    arzt.getSvnr(),
                    arzt.getFachgebiet(),
                    arzt.getAdresse(),
                    arzt.getTelefonNummer(),
                    arzt.getEmail()
            );
            System.out.println("=== ARZT GESPEICHERT ===");
            System.out.println("ID: " + savedArzt.getId());
            return "redirect:/www/aerzte";
        } catch (Exception e) {
            System.out.println("=== FEHLER BEIM SPEICHERN ===");
            e.printStackTrace();
            model.addAttribute("error", "Fehler beim Speichern des Arztes: " + e.getMessage());
            return "aerzte/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Arzt arzt = arztService.getArzt(String.valueOf(id))
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + id + " nicht gefunden"));

        if (arzt.getEmail() == null) arzt.setEmail(new Email());
        if (arzt.getAdresse() == null) arzt.setAdresse(new Adresse());
        if (arzt.getTelefonNummer() == null) arzt.setTelefonNummer(new TelefonNummer());

        model.addAttribute("arzt", arzt);
        return "aerzte/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("arzt") ArztFormDto arzt,
                         BindingResult result, Model model) {

        System.out.println("=== DEBUG: Update empfangen für ID " + id + " ===");

        if (result.hasErrors()) {
            System.out.println("Validation errors beim Update:");
            result.getAllErrors().forEach(error ->
                    System.out.println("Error: " + error.getDefaultMessage()));
            return "aerzte/form";
        }

        try {
            arztService.updateArzt(
                    String.valueOf(arzt.getArztid()),
                    arzt.getName(),
                    arzt.getGeburtsdatum(),
                    arzt.getSvnr(),
                    arzt.getFachgebiet(),
                    arzt.getAdresse(),
                    arzt.getTelefonnummer(),
                    arzt.getEmail()
            );
            System.out.println("Arzt mit ID " + id + " erfolgreich aktualisiert");
            return "redirect:/www/aerzte";
        } catch (Exception e) {
            System.out.println("Fehler beim Update:");
            e.printStackTrace();
            model.addAttribute("error", "Fehler beim Aktualisieren: " + e.getMessage());
            return "aerzte/form";
        }
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        Long arztId;

        try {
            arztId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            if (id.startsWith("ArztId[id=") && id.endsWith("]")) {
                String idStr = id.substring(10, id.length() - 1);
                arztId = Long.parseLong(idStr);
            } else {
                throw new IllegalArgumentException("Ungültige ID: " + id);
            }
        }

        arztService.deleteArzt(String.valueOf(arztId));
        return "redirect:/www/aerzte";
    }
}
