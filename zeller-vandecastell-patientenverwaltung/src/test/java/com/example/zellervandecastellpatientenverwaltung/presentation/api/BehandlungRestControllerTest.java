package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.service.BehandlungService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BehandlungRestController.class)
class BehandlungRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BehandlungService behandlungService;

    @Test
    void getAllBehandlungen_shouldReturnOk() throws Exception {
        when(behandlungService.getAll()).thenReturn(List.of(FixturesFactory.Behandlung1()));

        mockMvc.perform(get("/behandlung"))
                .andExpect(status().isOk());
    }

    @Test
    void getBehandlung_existing_shouldReturnOk() throws Exception {
        when(behandlungService.getBehandlung(anyLong()))
                .thenReturn(Optional.of(FixturesFactory.Behandlung1()));

        mockMvc.perform(get("/behandlung/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBehandlung_notExisting_shouldReturnNotFound() throws Exception {
        when(behandlungService.getBehandlung(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/behandlung/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBehandlung_shouldReturnOk() throws Exception {
        var now = LocalDateTime.now();
        var ende = now.plusHours(1);

        when(behandlungService.createBehandlung(any(), any(), anyString(), anyLong(), anyLong()))
                .thenReturn(FixturesFactory.Behandlung1());

        mockMvc.perform(post("/behandlung")
                        .param("beginn", now.toString())
                        .param("ende", ende.toString())
                        .param("diagnose", "Testdiagnose")
                        .param("arztId", "1")
                        .param("patientId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateBehandlung_shouldReturnOk() throws Exception {
        var now = LocalDateTime.now();
        var ende = now.plusHours(1);

        when(behandlungService.updateBehandlung(anyLong(), any(), any(), any(), any(), any()))
                .thenReturn(FixturesFactory.Behandlung1());

        mockMvc.perform(put("/behandlung/1")
                        .param("beginn", now.toString())
                        .param("ende", ende.toString())
                        .param("diagnose", "Neue Diagnose")
                        .param("arztId", "1")
                        .param("patientId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBehandlung_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(behandlungService).deleteBehandlung(anyLong());

        mockMvc.perform(delete("/behandlung/1"))
                .andExpect(status().isNoContent());
    }
}
