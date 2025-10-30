package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import com.example.zellervandecastellpatientenverwaltung.service.WarteRaumService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarteRaumRestController.class)
class WarteRaumRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarteRaumService warteRaumService;

    @Test
    void getAllWarteraeume_shouldReturnList() throws Exception {
        Mockito.when(warteRaumService.getAll()).thenReturn(List.of(FixturesFactory.WarteRaum22Seats()));

        mockMvc.perform(get("/warteraum"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].anzahlSitzplaetze").value(22));
    }

    @Test
    void getWarteraum_existing_shouldReturnOk() throws Exception {
        Mockito.when(warteRaumService.getWarteraum(anyLong())).thenReturn(Optional.of(FixturesFactory.WarteRaumMitSitzplaetzen(10)));

        mockMvc.perform(get("/warteraum/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anzahlSitzplaetze").value(10));
    }

    @Test
    void getWarteraum_notExisting_shouldReturnNotFound() throws Exception {
        Mockito.when(warteRaumService.getWarteraum(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/warteraum/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWarteraum_shouldReturnOk() throws Exception {
        Mockito.when(warteRaumService.createWarteraum(anyInt())).thenReturn(FixturesFactory.WarteRaumMitSitzplaetzen(15));

        mockMvc.perform(post("/warteraum")
                        .param("anzahlSitzplaetze", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anzahlSitzplaetze").value(15));
    }

    @Test
    void updateWarteraum_shouldReturnOk() throws Exception {
        Mockito.when(warteRaumService.updateWarteraum(anyLong(), anyInt())).thenReturn(FixturesFactory.WarteRaumMitSitzplaetzen(18));

        mockMvc.perform(put("/warteraum/1")
                        .param("neueAnzahlSitzplaetze", "18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anzahlSitzplaetze").value(18));
    }

    @Test
    void deleteWarteraum_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(warteRaumService).deleteWarteraum(anyLong());

        mockMvc.perform(delete("/warteraum/1"))
                .andExpect(status().isNoContent());
    }
}
