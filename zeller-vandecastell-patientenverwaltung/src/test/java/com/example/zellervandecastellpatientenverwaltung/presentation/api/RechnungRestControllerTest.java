package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.domain.*;
import com.example.zellervandecastellpatientenverwaltung.service.RechnungService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RechnungRestController.class)
class RechnungRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RechnungService rechnungService;

    @Test
    void getAllRechnungen_shouldReturnList() throws Exception {
        Mockito.when(rechnungService.getAll()).thenReturn(List.of(FixturesFactory.StandardRechnung()));

        mockMvc.perform(get("/rechnung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].betrag").value(150.0))
                .andExpect(jsonPath("$[0].bezahlt").value(false))
                .andExpect(jsonPath("$[0].patient.name").value("Max Mustermann"))
                .andExpect(jsonPath("$[0].arzt.name").value("Dr. House"));
    }

    @Test
    void getRechnung_existing_shouldReturnOk() throws Exception {
        var rechnung = FixturesFactory.BeispielRechnung(200.0, true, LocalDateTime.of(2024, 5, 1, 8, 30));
        Mockito.when(rechnungService.getRechnung(anyLong())).thenReturn(Optional.of(rechnung));

        mockMvc.perform(get("/rechnung/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betrag").value(200.0))
                .andExpect(jsonPath("$.bezahlt").value(true));
    }

    @Test
    void getRechnung_notExisting_shouldReturnNotFound() throws Exception {
        Mockito.when(rechnungService.getRechnung(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/rechnung/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRechnung_shouldReturnOk() throws Exception {
        var rechnung = FixturesFactory.BeispielRechnung(120.0, false, LocalDateTime.of(2024, 6, 1, 12, 0));
        Mockito.when(rechnungService.createRechnung(anyLong(), anyLong(), anyDouble(), any(), anyBoolean()))
                .thenReturn(rechnung);

        mockMvc.perform(post("/rechnung")
                        .param("patientId", "1")
                        .param("arztId", "2")
                        .param("betrag", "120.0")
                        .param("datum", "2024-06-01T12:00:00")
                        .param("bezahlt", "false")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betrag").value(120.0))
                .andExpect(jsonPath("$.bezahlt").value(false));
    }

    @Test
    void updateRechnung_shouldReturnOk() throws Exception {
        var updated = FixturesFactory.BeispielRechnung(222.0, true, LocalDateTime.of(2024, 5, 1, 8, 30));
        Mockito.when(rechnungService.updateRechnung(anyLong(), anyDouble(), anyBoolean())).thenReturn(updated);

        mockMvc.perform(put("/rechnung/1")
                        .param("neuerBetrag", "222.0")
                        .param("bezahlt", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betrag").value(222.0))
                .andExpect(jsonPath("$.bezahlt").value(true));
    }

    @Test
    void deleteRechnung_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(rechnungService).deleteRechnung(anyLong());

        mockMvc.perform(delete("/rechnung/1"))
                .andExpect(status().isNoContent());
    }
}
