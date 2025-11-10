package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import com.example.zellervandecastellpatientenverwaltung.dtos.PatientFormDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.TelefonNummerFormDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
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
@RequestMapping("/www/patienten")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public String getPatienten(Model model) {
        model.addAttribute("patienten", patientService.getAll());
        return "patienten/index";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        PatientFormDto dto = new PatientFormDto();
        dto.setAdresse(new Adresse());
        dto.setTelefonnummer(new TelefonNummerFormDto());
        model.addAttribute("patient", dto);
        return "patienten/form";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("patient") PatientFormDto patientDto,
                       BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "patienten/form";
        }

        try {
            TelefonNummer tel = TelefonNummer.fromString(
                patientDto.getTelefonnummer() != null ? patientDto.getTelefonnummer().getValue() : null
            );

            patientService.createPatient(
                    patientDto.getName(),
                    patientDto.getGeburtsdatum(),
                    patientDto.getSvnr(),
                    patientDto.getVersicherungsart(),
                    patientDto.getAdresse(),
                    tel
            );

            return "redirect:/www/patienten";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Speichern des Patienten: " + e.getMessage());
            return "patienten/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Patient patient = patientService.getPatient(id)
                .orElseThrow(() -> new NotFoundException("Patient mit ID " + id + " nicht gefunden"));

        log.info("Patient geladen - Name: {}, Geburtsdatum: {}", patient.getName(), patient.getGebDatum());

        PatientFormDto dto = new PatientFormDto();
        dto.setPatientId(patient.getId());
        dto.setName(patient.getName());
        dto.setGeburtsdatum(patient.getGebDatum());
        dto.setSvnr(patient.getSvnr());
        dto.setVersicherungsart(patient.getVersicherungsart());
        dto.setAdresse(patient.getAdresse());

        TelefonNummerFormDto telDto = new TelefonNummerFormDto();
        if (patient.getTelefonNummer() != null) {
            telDto.setValue(patient.getTelefonNummer().toFormattedString());
        }
        dto.setTelefonnummer(telDto);

        log.info("DTO erstellt - Geburtsdatum: {}", dto.getGeburtsdatum());

        model.addAttribute("patient", dto);
        return "patienten/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") String id,
                         @Valid @ModelAttribute("patient") PatientFormDto patientDto,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "patienten/form";
        }

        try {
            TelefonNummer tel = TelefonNummer.fromString(
                patientDto.getTelefonnummer() != null ? patientDto.getTelefonnummer().getValue() : null
            );

            patientService.updatePatient(
                    id,
                    patientDto.getName(),
                    patientDto.getGeburtsdatum(),
                    patientDto.getSvnr(),
                    patientDto.getVersicherungsart(),
                    patientDto.getAdresse(),
                    tel
            );
            return "redirect:/www/patienten";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Aktualisieren: " + e.getMessage());
            return "patienten/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        patientService.deletePatient(id);
        return "redirect:/www/patienten";
    }
}
