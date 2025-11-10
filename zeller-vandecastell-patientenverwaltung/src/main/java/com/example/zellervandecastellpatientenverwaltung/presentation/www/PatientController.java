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
            TelefonNummer tel = createTelefonNummerSafe(patientDto.getTelefonnummer() != null ? patientDto.getTelefonnummer().getValue() : null);

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
    public String editForm(@PathVariable String id, Model model) {
        Patient patient = patientService.getPatient(id)
                .orElseThrow(() -> new NotFoundException("Patient mit ID " + id + " nicht gefunden"));

        PatientFormDto dto = new PatientFormDto();
        dto.setPatientId(Long.valueOf(patient.getId()));
        dto.setName(patient.getName());
        dto.setGeburtsdatum(patient.getGebDatum());
        dto.setSvnr(patient.getSvnr());
        dto.setVersicherungsart(patient.getVersicherungsart());
        dto.setAdresse(patient.getAdresse());

        TelefonNummerFormDto telDto = new TelefonNummerFormDto();
        try {
            if (patient.getTelefonNummer() != null) {
                try {
                    var m = TelefonNummer.class.getMethod("getValue");
                    Object v = m.invoke(patient.getTelefonNummer());
                    if (v != null) telDto.setValue(String.valueOf(v));
                } catch (NoSuchMethodException ignore) {
                    var f = TelefonNummer.class.getDeclaredField("value");
                    f.setAccessible(true);
                    Object v = f.get(patient.getTelefonNummer());
                    if (v != null) telDto.setValue(String.valueOf(v));
                }
            }
        } catch (Exception ignore) {}
        dto.setTelefonnummer(telDto);

        model.addAttribute("patient", dto);
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
            TelefonNummer tel = createTelefonNummerSafe(patientDto.getTelefonnummer() != null ? patientDto.getTelefonnummer().getValue() : null);

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
    public String delete(@PathVariable String id) {
        patientService.deletePatient(id);
        return "redirect:/www/patienten";
    }

    private TelefonNummer createTelefonNummerSafe(String value) {
        try {
            if (value == null || value.isBlank()) return null;
            try {
                var m = TelefonNummer.class.getMethod("of", String.class);
                return (TelefonNummer) m.invoke(null, value);
            } catch (NoSuchMethodException ignored) {}
            try {
                var m = TelefonNummer.class.getMethod("from", String.class);
                return (TelefonNummer) m.invoke(null, value);
            } catch (NoSuchMethodException ignored) {}
            try {
                var ctor = TelefonNummer.class.getConstructor(String.class);
                return (TelefonNummer) ctor.newInstance(value);
            } catch (NoSuchMethodException ignored) {}
            try {
                var ctor0 = TelefonNummer.class.getDeclaredConstructor();
                ctor0.setAccessible(true);
                Object obj = ctor0.newInstance();
                try {
                    var setter = TelefonNummer.class.getMethod("setValue", String.class);
                    setter.invoke(obj, value);
                    return (TelefonNummer) obj;
                } catch (NoSuchMethodException e) {
                    var f = TelefonNummer.class.getDeclaredField("value");
                    f.setAccessible(true);
                    f.set(obj, value);
                    return (TelefonNummer) obj;
                }
            } catch (NoSuchMethodException ignored) {}
            throw new IllegalStateException("TelefonNummer kann nicht aus String erzeugt werden");
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
