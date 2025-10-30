package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.service.MitarbeiterService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MitarbeiterRestController.class)
class MitarbeiterRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MitarbeiterService mitarbeiterService;

    @Test
    void getAllMitarbeiter_shouldReturnOk() throws Exception {
        when(mitarbeiterService.getAll()).thenReturn(List.of(FixturesFactory.MitarbeiterMaxMuster()));

        mockMvc.perform(get("/mitarbeiter"))
                .andExpect(status().isOk());
    }

    @Test
    void getMitarbeiter_existing_shouldReturnOk() throws Exception {
        when(mitarbeiterService.getMitarbeiter(anyLong()))
                .thenReturn(Optional.of(FixturesFactory.MitarbeiterMaxMuster()));

        mockMvc.perform(get("/mitarbeiter/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMitarbeiter_notExisting_shouldReturnNotFound() throws Exception {
        when(mitarbeiterService.getMitarbeiter(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/mitarbeiter/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMitarbeiter_shouldReturnOk() throws Exception {
        when(mitarbeiterService.createMitarbeiter(any(), any(), any(), any()))
                .thenReturn(FixturesFactory.MitarbeiterMaxMuster());

        mockMvc.perform(post("/mitarbeiter")
                        .param("name", "Max Mustermann")
                        .param("gebDatum", "1970-01-01")
                        .param("svnr", "1234567890")
                        .param("gehalt", "2000"))
                .andExpect(status().isOk());
    }

    @Test
    void updateMitarbeiter_shouldReturnOk() throws Exception {
        when(mitarbeiterService.updateMitarbeiter(anyLong(), any(), any(), any(), any()))
                .thenReturn(FixturesFactory.MitarbeiterMaxMuster());

        mockMvc.perform(put("/mitarbeiter/1")
                        .param("name", "Max Mustermann")
                        .param("gebDatum", "1970-01-01")
                        .param("svnr", "1234567890")
                        .param("gehalt", "2000"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMitarbeiter_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(mitarbeiterService).deleteMitarbeiter(anyLong());

        mockMvc.perform(delete("/mitarbeiter/1"))
                .andExpect(status().isNoContent());
    }
}
