package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztFormDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.TelefonNummerFormDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
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
        ArztFormDto dto = new ArztFormDto();
        dto.setAdresse(new Adresse());
        dto.setTelefonnummer(new TelefonNummerFormDto());
        dto.setEmail(new Email());
        model.addAttribute("arzt", dto);
        return "aerzte/form";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("arzt") ArztFormDto arztDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "aerzte/form";
        }

        try {
            TelefonNummer tel = TelefonNummer.fromString(
                arztDto.getTelefonnummer() != null ? arztDto.getTelefonnummer().getValue() : null
            );

            arztService.createArzt(
                    arztDto.getName(),
                    arztDto.getGeburtsdatum(),
                    arztDto.getSvnr(),
                    arztDto.getFachgebiet(),
                    arztDto.getAdresse(),
                    tel,
                    arztDto.getEmail()
            );
            return "redirect:/www/aerzte";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Speichern des Arztes: " + e.getMessage());
            return "aerzte/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Arzt arzt = arztService.getArzt(id)
                .orElseThrow(() -> new NotFoundException("Arzt mit ID " + id + " nicht gefunden"));

        log.info("Arzt geladen - Name: {}, Geburtsdatum: {}", arzt.getName(), arzt.getGebDatum());

        ArztFormDto dto = new ArztFormDto();
        dto.setArztid(arzt.getId());
        dto.setName(arzt.getName());
        dto.setGeburtsdatum(arzt.getGebDatum());
        dto.setSvnr(arzt.getSvnr());
        dto.setFachgebiet(arzt.getFachgebiet());
        dto.setAdresse(arzt.getAdresse() != null ? arzt.getAdresse() : new Adresse());
        dto.setEmail(arzt.getEmail() != null ? arzt.getEmail() : new Email());

        TelefonNummerFormDto telDto = new TelefonNummerFormDto();
        if (arzt.getTelefonNummer() != null) {
            telDto.setValue(arzt.getTelefonNummer().toFormattedString());
        }
        dto.setTelefonnummer(telDto);

        log.info("DTO erstellt - Geburtsdatum: {}", dto.getGeburtsdatum());

        model.addAttribute("arzt", dto);
        return "aerzte/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") String id,
                         @Valid @ModelAttribute("arzt") ArztFormDto arztDto,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "aerzte/form";
        }

        try {
            TelefonNummer tel = TelefonNummer.fromString(
                arztDto.getTelefonnummer() != null ? arztDto.getTelefonnummer().getValue() : null
            );

            arztService.updateArzt(
                    id,
                    arztDto.getName(),
                    arztDto.getGeburtsdatum(),
                    arztDto.getSvnr(),
                    arztDto.getFachgebiet(),
                    arztDto.getAdresse(),
                    tel,
                    arztDto.getEmail()
            );
            return "redirect:/www/aerzte";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Aktualisieren: " + e.getMessage());
            return "aerzte/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        arztService.deleteArzt(id);
        return "redirect:/www/aerzte";
    }
}
