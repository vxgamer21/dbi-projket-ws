package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungFormDto;
import com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException;
import com.example.zellervandecastellpatientenverwaltung.service.ArztService;
import com.example.zellervandecastellpatientenverwaltung.service.BehandlungService;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Log4j2
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
        model.addAttribute("behandlung", new BehandlungFormDto());
        model.addAttribute("aerzte", arztService.getAll());
        model.addAttribute("patienten", patientService.getAll());
        return "behandlungen/form";
    }




    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Behandlung behandlung = behandlungService.getBehandlung(id)
                .orElseThrow(() -> new NotFoundException("Behandlung mit ID " + id + " nicht gefunden"));

        log.info("Behandlung geladen - Beginn: {}, Ende: {}", behandlung.getBeginn(), behandlung.getEnde());

        BehandlungFormDto dto = new BehandlungFormDto();
        dto.setId(behandlung.getId());
        dto.setBeginn(behandlung.getBeginn());
        dto.setEnde(behandlung.getEnde());
        dto.setDiagnose(behandlung.getDiagnose());
        dto.setArztId(behandlung.getArzt() != null ? behandlung.getArzt().getId() : null);
        dto.setPatientId(behandlung.getPatient() != null ? behandlung.getPatient().getId() : null);

        log.info("DTO erstellt - Beginn: {}, Ende: {}", dto.getBeginn(), dto.getEnde());

        model.addAttribute("behandlung", dto);
        model.addAttribute("aerzte", arztService.getAll());
        model.addAttribute("patienten", patientService.getAll());
        return "behandlungen/form";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
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
            String arztId = behandlungDto.getArztId();
            String patientId = behandlungDto.getPatientId();

            behandlungService.createBehandlung(
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
    public String update(@PathVariable("id") String id,
                         @Valid @ModelAttribute("behandlung") BehandlungFormDto behandlungDto,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("aerzte", arztService.getAll());
            model.addAttribute("patienten", patientService.getAll());
            return "behandlungen/form";
        }

        try {
            String arztId = behandlungDto.getArztId();
            String patientId = behandlungDto.getPatientId();

            behandlungService.updateBehandlung(
                    id,
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
