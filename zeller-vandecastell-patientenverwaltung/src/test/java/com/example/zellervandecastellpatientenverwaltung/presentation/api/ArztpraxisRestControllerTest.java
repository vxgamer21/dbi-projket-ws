package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import com.example.zellervandecastellpatientenverwaltung.FixturesFactory;
import com.example.zellervandecastellpatientenverwaltung.domain.Arztpraxis;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummerArt;
import com.example.zellervandecastellpatientenverwaltung.dtos.*;
import com.example.zellervandecastellpatientenverwaltung.service.ArztpraxisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArztpraxisRestController.class)
class ArztpraxisRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArztpraxisService arztpraxisService;

    @Test
    void getAllArztpraxen_shouldReturnList() throws Exception {
        when(arztpraxisService.getAll()).thenReturn(List.of(FixturesFactory.ArztpraxisStandard()));
        mockMvc.perform(get("/arztpraxis"))
                .andExpect(status().isOk());
    }

    @Test
    void getArztpraxis_existing_shouldReturnOk() throws Exception {
        when(arztpraxisService.getArztpraxis(anyLong()))
                .thenReturn(Optional.of(FixturesFactory.ArztpraxisStandard()));
        mockMvc.perform(get("/arztpraxis/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getArztpraxis_notExisting_shouldReturn404() throws Exception {
        when(arztpraxisService.getArztpraxis(anyLong()))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/arztpraxis/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createArztpraxis_shouldReturnCreated() throws Exception {
        when(arztpraxisService.createArztpraxis(any(), anyBoolean(), any(), any(), any(), any()))
                .thenReturn(FixturesFactory.ArztpraxisStandard());
        mockMvc.perform(post("/arztpraxis")
                        .contentType("application/json")
                        .content("{\"name\": \"Testpraxis\", \"istKassenarzt\": true, \"arztId\": 1}"))
                .andExpect(status().isCreated());
    }
}


