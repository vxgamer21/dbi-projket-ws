package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.dtos.PatientDto;
import com.example.zellervandecastellpatientenverwaltung.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientRestController.class)
class PatientRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    void getAllPatienten_shouldReturnList() throws Exception {
        Patient patient = Patient.builder()
                .name("Max Mustermann")
                .gebDatum(LocalDate.of(1990, 1, 1))
                .svnr(123456789L)
                .versicherungsart(Versicherungsart.PRIVAT)
                .adresse(new Adresse("Teststr", "1", "1010", "Wien"))
                .telefonNummer(new TelefonNummer("+43", "664", "1234567", TelefonNummerArt.MOBIL))
                .build();

        Mockito.when(patientService.getAll()).thenReturn(List.of(patient));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Max Mustermann"))
                .andExpect(jsonPath("$[0].svnr").value(123456789L));
    }

    @Test
    void getPatient_existing_shouldReturnOk() throws Exception {
        Patient patient = Patient.builder()
                .name("Max Mustermann")
                .gebDatum(LocalDate.of(1990, 1, 1))
                .svnr(123456789L)
                .versicherungsart(Versicherungsart.KRANKENKASSE)
                .adresse(new Adresse("Teststr", "1", "1010", "Wien"))
                .telefonNummer(new TelefonNummer("+43", "664", "1234567", TelefonNummerArt.MOBIL))
                .build();

        Mockito.when(patientService.getPatient(anyLong())).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Max Mustermann"))
                .andExpect(jsonPath("$.svnr").value(123456789L));
    }

    @Test
    void getPatient_notExisting_shouldReturnNotFound() throws Exception {
        Mockito.when(patientService.getPatient(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/patient/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePatient_shouldReturnOk() throws Exception {
        Patient updated = Patient.builder()
                .name("Anna Musterfrau Neu")
                .gebDatum(LocalDate.of(1990, 1, 1))
                .svnr(555555555L)
                .versicherungsart(Versicherungsart.KRANKENKASSE)
                .adresse(new Adresse("Neu", "2", "1100", "Wien"))
                .telefonNummer(new TelefonNummer("+43", "660", "5555555", TelefonNummerArt.MOBIL))
                .build();

        Mockito.when(patientService.updatePatient(anyLong(), any(), any(), any(), any(), any(), any())).thenReturn(updated);

        mockMvc.perform(put("/patient/1")
                        .param("name", "Anna Musterfrau Neu")
                        .param("geburtsdatum", "1990-01-01")
                        .param("svnr", "555555555")
                        .param("versicherungsart", "KRANKENKASSE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"strasse\":\"Neu\",\"hausNr\":\"2\",\"plz\":\"1100\",\"stadt\":\"Wien\"}")
                        .content("{\"landesvorwahl\":\"+43\",\"netzvorwahl\":\"660\",\"nummer\":\"5555555\",\"art\":\"MOBIL\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anna Musterfrau Neu"))
                .andExpect(jsonPath("$.svnr").value(555555555L));
    }

    @Test
    void deletePatient_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(patientService).deletePatient(anyLong());

        mockMvc.perform(delete("/patient/1"))
                .andExpect(status().isNoContent());
    }
}
