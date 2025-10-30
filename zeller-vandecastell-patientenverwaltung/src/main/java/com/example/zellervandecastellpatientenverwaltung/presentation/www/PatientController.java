package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.dtos.PatientFormDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
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
        Patient patient = new Patient();
        patient.setAdresse(new Adresse());
        patient.setTelefonNummer(new TelefonNummer());
        model.addAttribute("patient", patient);

        return "patienten/form";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("patient") Patient patient,
                       BindingResult result, Model model) {

        if (patient.getAdresse() == null) patient.setAdresse(new Adresse());
        if (patient.getTelefonNummer() == null) patient.setTelefonNummer(new TelefonNummer());

        if (patient.getApiKey() == null || patient.getApiKey().trim().isEmpty()) {
            patient.setApiKey(ApiKeyGenerator.generateApiKey());
        }

        if (result.hasErrors()) {
            return "patienten/form";
        }

        try {
            Patient savedPatient = patientService.createPatient(
                    patient.getName(),
                    patient.getGebDatum(),
                    patient.getSvnr(),
                    patient.getVersicherungsart(),
                    patient.getAdresse(),
                    patient.getTelefonNummer()


            );
            System.out.println("Patient gespeichert: " + savedPatient.getId());
            return "redirect:/www/patienten";

        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Speichern des Patienten: " + e.getMessage());
            return "patienten/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatient(id)
                .orElseThrow(() -> new NotFoundException("Patient mit ID " + id + " nicht gefunden"));

        if (patient.getAdresse() == null) patient.setAdresse(new Adresse());
        if (patient.getTelefonNummer() == null) patient.setTelefonNummer(new TelefonNummer());

        model.addAttribute("patient", patient);
        return "patienten/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute("patient") PatientFormDto patientDto,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "patienten/form";
        }

        try {
            patientService.updatePatient(
                    id,
                    patientDto.getName(),
                    patientDto.getGeburtsdatum(),
                    patientDto.getSvnr(),
                    patientDto.getVersicherungsart(),
                    patientDto.getAdresse(),
                    patientDto.getTelefonnummer()
            );
            return "redirect:/www/patienten";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Aktualisieren: " + e.getMessage());
            return "patienten/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        patientService.deletePatient(id);
        return "redirect:/www/patienten";
    }
}
