package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.commands.ArztCommands;
import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztDto;
import com.example.zellervandecastellpatientenverwaltung.service.ArztService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArztRestController.class)
class ArztRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArztService arztService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAerzte_shouldReturnOk() throws Exception {
        when(arztService.getAll()).thenReturn(List.of(FixturesFactory.ArztAllgemein()));

        mockMvc.perform(get("/arzt"))
                .andExpect(status().isOk());
    }

    @Test
    void getBehandlungen_existing_shouldReturnOk() throws Exception {
        when(arztService.getArzt(anyLong()))
                .thenReturn(Optional.of(FixturesFactory.ArztAllgemein()));

        mockMvc.perform(get("/arzt/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBehandlungen_notExisting_shouldReturnNotFound() throws Exception {
        when(arztService.getArzt(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/arzt/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createArzt_shouldReturnCreated() throws Exception {
        var command = new ArztCommands.CreateArztCommand(
                "Dr. Test",
                Fachgebiet.ALLGEMEINMEDIZIN,
                LocalDate.of(1980, 1, 1),
                123456789L,
                FixturesFactory.Ringstrasse(),
                FixturesFactory.StandardMobil(),
                new Email("test@example.com")
        );

        when(arztService.createArzt(anyString(), any(), any(), any(), any(), any(), any()))
                .thenReturn(FixturesFactory.ArztAllgemein());

        mockMvc.perform(post("/arzt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateArzt_shouldReturnOk() throws Exception {
        var command = new ArztCommands.CreateArztCommand(
                "Dr. Test",
                Fachgebiet.ALLGEMEINMEDIZIN,
                LocalDate.of(1980, 1, 1),
                123456789L,
                FixturesFactory.Ringstrasse(),
                FixturesFactory.StandardMobil(),
                new Email("test@example.com")
        );

        when(arztService.updateArzt(anyLong(), anyString(), any(), any(), any(), any(), any(), any()))
                .thenReturn(FixturesFactory.ArztAllgemein());

        mockMvc.perform(put("/arzt/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteArzt_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(arztService).deleteArzt(anyLong());

        mockMvc.perform(delete("/arzt/1"))
                .andExpect(status().isNoContent());
    }
}
