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
    public String addForm(Model model) {
        var arzt = new Arzt();
        arzt.setAdresse(new Adresse());
        arzt.setTelefonNummer(new TelefonNummer());
        arzt.setEmail(new Email());
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
        }

        if (result.hasErrors()) {
            return "aerzte/form";
        }

        try {
            arztService.createArzt(
                    arzt.getName(),
                    arzt.getGebDatum(),
                    arzt.getSvnr(),
                    arzt.getFachgebiet(),
                    arzt.getAdresse(),
                    arzt.getTelefonNummer(),
                    arzt.getEmail()
            );
            return "redirect:/www/aerzte";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Speichern des Arztes: " + e.getMessage());
            return "aerzte/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Arzt arzt = arztService.getArzt(id)
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + id + " nicht gefunden"));

        if (arzt.getEmail() == null) arzt.setEmail(new Email());
        if (arzt.getAdresse() == null) arzt.setAdresse(new Adresse());
        if (arzt.getTelefonNummer() == null) arzt.setTelefonNummer(new TelefonNummer());

        model.addAttribute("arzt", arzt);
        return "aerzte/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute("arzt") ArztFormDto arzt,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "aerzte/form";
        }

        try {
            arztService.updateArzt(
                    id,
                    arzt.getName(),
                    arzt.getGeburtsdatum(),
                    arzt.getSvnr(),
                    arzt.getFachgebiet(),
                    arzt.getAdresse(),
                    arzt.getTelefonnummer(),
                    arzt.getEmail()
            );
            return "redirect:/www/aerzte";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Aktualisieren: " + e.getMessage());
            return "aerzte/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        arztService.deleteArzt(id);
        return "redirect:/www/aerzte";
    }
}
