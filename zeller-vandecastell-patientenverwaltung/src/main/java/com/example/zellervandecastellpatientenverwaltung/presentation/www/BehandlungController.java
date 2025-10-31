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

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/www/behandlungen")
public class BehandlungController {

    private final BehandlungService behandlungService;
    private final ArztService arztService;
    private final PatientService patientService;

    @GetMapping
    public String getBehandlungen(Model model) {
        var behandlungen = behandlungService.getAll();

        // Ärzte und Patienten mappen
        var aerzte = arztService.getAll().stream()
                .collect(Collectors.toMap(Arzt::getId, Arzt::getName));

        var patienten = patientService.getAll().stream()
                .collect(Collectors.toMap(Patient::getId, Patient::getName));

        model.addAttribute("behandlungen", behandlungen);
        model.addAttribute("aerzteMap", aerzte);
        model.addAttribute("patientenMap", patienten);

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
        Behandlung behandlung = behandlungService.getBehandlung(String.valueOf(id))
                .orElseThrow(() -> new NotFoundException("Behandlung mit ID " + id + " nicht gefunden"));

        BehandlungFormDto dto = new BehandlungFormDto();
        dto.setId((behandlung.getId()));
        dto.setBeginn(behandlung.getBeginn());
        dto.setEnde(behandlung.getEnde());
        dto.setDiagnose(behandlung.getDiagnose());
        dto.setArztId(behandlung.getArztId() != null ? (behandlung.getArztId()) : null);
        dto.setPatientId(behandlung.getPatientId() != null ? (behandlung.getPatientId()) : null);

        model.addAttribute("behandlung", dto);
        model.addAttribute("aerzte", arztService.getAll());
        model.addAttribute("patienten", patientService.getAll());
        return "behandlungen/form";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        behandlungService.deleteBehandlung(String.valueOf(id));
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
            String arztId = behandlungDto.getArztId() != null ? String.valueOf(behandlungDto.getArztId()) : null;
            String patientId = behandlungDto.getPatientId() != null ? String.valueOf(behandlungDto.getPatientId()) : null;

            Behandlung savedBehandlung = behandlungService.createBehandlung(
                    behandlungDto.getBeginn(),
                    behandlungDto.getEnde(),
                    behandlungDto.getDiagnose(),
                    arztId,
                    patientId
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
            String arztId = behandlungDto.getArztId() != null ? String.valueOf(behandlungDto.getArztId()) : null;
            String patientId = behandlungDto.getPatientId() != null ? String.valueOf(behandlungDto.getPatientId()) : null;

            Behandlung updatedBehandlung = behandlungService.updateBehandlung(
                    String.valueOf(id),
                    behandlungDto.getBeginn(),
                    behandlungDto.getEnde(),
                    behandlungDto.getDiagnose(),
                    arztId,
                    patientId
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
