package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungFormDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;
import com.example.zellervandecastellpatientenverwaltung.service.ArztService;
import com.example.zellervandecastellpatientenverwaltung.service.BehandlungService;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/www/behandlungen")
public class BehandlungController {

    private final BehandlungService behandlungService;
    private final ArztService arztService;
    private final PatientService patientService;

    @GetMapping
    public String getBehandlungen(Model model) {
        model.addAttribute("behandlungen", behandlungService.getAll());
        return "behandlungen/index";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("behandlung", new Behandlung());
        model.addAttribute("aerzte", arztService.getAll());
        model.addAttribute("patienten", patientService.getAll());
        return "behandlungen/form";
    }



    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Behandlung behandlung = behandlungService.getBehandlung(id)
                .orElseThrow(() -> new NotFoundException("Behandlung mit ID " + id + " nicht gefunden"));

        BehandlungFormDto dto = new BehandlungFormDto();
        dto.setBehandlungId(behandlung.getId());
        dto.setBeginn(behandlung.getBeginn());
        dto.setEnde(behandlung.getEnde());
        dto.setDiagnose(behandlung.getDiagnose());
        dto.setArztId(behandlung.getArzt() != null ? behandlung.getArzt().getId() : null);
        dto.setPatientId(behandlung.getPatient() != null ? behandlung.getPatient().getId() : null);

        model.addAttribute("behandlung", dto);
        model.addAttribute("aerzte", arztService.getAll());
        model.addAttribute("patienten", patientService.getAll());
        return "behandlungen/form";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        behandlungService.deleteBehandlung(id);
        return "redirect:/www/behandlungen";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("behandlung") BehandlungFormDto behandlungDto,
                       BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("aerzte", arztService.getAll());
            model.addAttribute("patienten", patientService.getAll());
            return "behandlungen/form";
        }

        try {
            Behandlung savedBehandlung = behandlungService.createBehandlung(
                    behandlungDto.getBeginn(),
                    behandlungDto.getEnde(),
                    behandlungDto.getDiagnose(),
                    behandlungDto.getArztId(),
                    behandlungDto.getPatientId()
            );
            return "redirect:/www/behandlungen";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Speichern der Behandlung: " + e.getMessage());
            model.addAttribute("aerzte", arztService.getAll());
            model.addAttribute("patienten", patientService.getAll());
            return "behandlungen/form";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("behandlung") BehandlungFormDto behandlungDto,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("aerzte", arztService.getAll());
            model.addAttribute("patienten", patientService.getAll());
            return "behandlungen/form";
        }

        try {
            Behandlung updatedBehandlung = behandlungService.updateBehandlung(
                    id,
                    behandlungDto.getBeginn(),
                    behandlungDto.getEnde(),
                    behandlungDto.getDiagnose(),
                    behandlungDto.getArztId(),
                    behandlungDto.getPatientId()
            );
            return "redirect:/www/behandlungen";
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Aktualisieren der Behandlung: " + e.getMessage());
            model.addAttribute("aerzte", arztService.getAll());
            model.addAttribute("patienten", patientService.getAll());
            return "behandlungen/form";
        }
    }
}
