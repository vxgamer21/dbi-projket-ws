package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.service.BehandlungsRaumService;
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

@WebMvcTest(BehandlungsRaumRestController.class)
class BehandlungsRaumRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BehandlungsRaumService behandlungsRaumService;

    @Test
    void getAllBehandlungsRaeume_shouldReturnOk() throws Exception {
        when(behandlungsRaumService.getAll()).thenReturn(List.of(FixturesFactory.BehandlungsRaum1Frei()));

        mockMvc.perform(get("/behandlungsraum"))
                .andExpect(status().isOk());
    }

    @Test
    void getBehandlungsRaum_existing_shouldReturnOk() throws Exception {
        when(behandlungsRaumService.getBehandlungsraum(anyLong()))
                .thenReturn(Optional.of(FixturesFactory.BehandlungsRaum1Frei()));

        mockMvc.perform(get("/behandlungsraum/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBehandlungsRaum_notExisting_shouldReturnNotFound() throws Exception {
        when(behandlungsRaumService.getBehandlungsraum(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/behandlungsraum/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBehandlungsRaum_shouldReturnOk() throws Exception {
        when(behandlungsRaumService.createBehandlungsraum(anyLong(), any(), anyBoolean()))
                .thenReturn(FixturesFactory.BehandlungsRaum1Frei());

        mockMvc.perform(post("/behandlungsraum")
                        .param("behandlungId", "1")
                        .param("ausstattung", "Testausstattung")
                        .param("isFrei", "true"))
                .andExpect(status().isCreated());
    }

    @Test
    void updateBehandlungsRaum_shouldReturnOk() throws Exception {
        when(behandlungsRaumService.updateBehandlungsraum(anyLong(), any(), anyBoolean()))
                .thenReturn(FixturesFactory.BehandlungsRaum1Frei());

        mockMvc.perform(put("/behandlungsraum/1")
                        .param("ausstattung", "Neue Ausstattung")
                        .param("isFrei", "false"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBehandlungsRaum_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(behandlungsRaumService).deleteBehandlungsraum(anyLong());

        mockMvc.perform(delete("/behandlungsraum/1"))
                .andExpect(status().isNoContent());
    }
}
