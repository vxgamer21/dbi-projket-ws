package com.example.zellervandecastellpatientenverwaltung.presentation.www;


import com.example.zellervandecastellpatientenverwaltung.service.ArztService;


import com.example.zellervandecastellpatientenverwaltung.service.BehandlungService;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/www")
public class HomeController {

    private final ArztService arztService;
    private final PatientService patientService;
    private final BehandlungService behandlungService;


    @GetMapping({"", "/"})
    public String home(Model model) {
        try {
            var aerzte = arztService.getAll();
            var patienten = patientService.getAll();
            var behandlung = behandlungService.getAll();
            model.addAttribute("aerzte", aerzte);
            model.addAttribute("patienten",patienten);
            model.addAttribute("behandlungen", behandlung);



            System.out.println("Homepage geladen - Anzahl Ärzte: " + aerzte.size());

        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Homepage-Daten: " + e.getMessage());
            e.printStackTrace();
        }

        return "home/home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "redirect:/www";
    }
}