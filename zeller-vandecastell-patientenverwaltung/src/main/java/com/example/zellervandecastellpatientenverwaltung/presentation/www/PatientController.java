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
    public String save(@Valid @ModelAttribute("patient") Patient patient, BindingResult result, Model model) {

        if (patient.getAdresse() == null) patient.setAdresse(new Adresse());
        if (patient.getTelefonNummer() == null) patient.setTelefonNummer(new TelefonNummer());

        if (patient.getApiKey() == null || patient.getApiKey().trim().isEmpty()) {
            patient.setApiKey(ApiKeyGenerator.generateApiKey());
            System.out.println("Generated API Key: " + patient.getApiKey());
        }

        System.out.println("=== DEBUG: Formular empfangen ===");
        System.out.println("Name: " + patient.getName());
        System.out.println("SVNR: " + patient.getSvnr());
        System.out.println("Geburtsdatum: " + patient.getGebDatum());
        System.out.println("Adresse: " + (patient.getAdresse() != null ? patient.getAdresse().toString() : "null"));
        System.out.println("TelefonNummer: " + (patient.getTelefonNummer() != null ? patient.getTelefonNummer().toString() : "null"));

        if (result.hasErrors()) {
            System.out.println("=== VALIDATION ERRORS ===");
            result.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
                if (error instanceof org.springframework.validation.FieldError fieldError) {
                    System.out.println("Field: " + fieldError.getField() + " - Value: " + fieldError.getRejectedValue());
                }
            });
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
            System.out.println("=== Patient GESPEICHERT ===");
            System.out.println("ID: " + savedPatient.getPatientID());
            return "redirect:/www/patienten";
        } catch (Exception e) {
            System.out.println("=== FEHLER BEIM SPEICHERN ===");
            e.printStackTrace();
            model.addAttribute("error", "Fehler beim Speichern des Patienten: " + e.getMessage());
            return "patienten/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatient(id)
                .orElseThrow(() -> new NotFoundException("Patient mit ID " + id + " nicht gefunden"));

        if (patient.getAdresse() == null) patient.setAdresse(new Adresse());
        if (patient.getTelefonNummer() == null) patient.setTelefonNummer(new TelefonNummer());

        model.addAttribute("patient", patient);
        return "patienten/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("patient") PatientFormDto patient,
                         BindingResult result, Model model) {

        System.out.println("=== DEBUG: Update empfangen für ID " + id + " ===");

        if (result.hasErrors()) {
            System.out.println("Validation errors beim Update:");
            result.getAllErrors().forEach(error ->
                    System.out.println("Error: " + error.getDefaultMessage()));
            return "patienten/form";
        }

        try {
            patientService.updatePatient(
                    id,
                    patient.getName(),
                    patient.getGeburtsdatum(),
                    patient.getSvnr(),
                    patient.getVersicherungsart(),
                    patient.getAdresse(),
                    patient.getTelefonnummer()
            );
            System.out.println("Patient mit ID " + id + " erfolgreich aktualisiert");
            return "redirect:/www/patienten";
        } catch (Exception e) {
            System.out.println("Fehler beim Update:");
            e.printStackTrace();
            model.addAttribute("error", "Fehler beim Aktualisieren: " + e.getMessage());
            return "patienten/form";
        }
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        Long patientId;

        try {
            patientId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            if (id.startsWith("PatientID[id=") && id.endsWith("]")) {
                String idStr = id.substring(10, id.length() - 1);
                patientId = Long.parseLong(idStr);
            } else {
                throw new IllegalArgumentException("Ungültige ID: " + id);
            }
        }

        patientService.deletePatient(patientId);
        return "redirect:/www/patienten";
    }
}
