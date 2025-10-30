package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ArztDtos {

    public record Minimal(String name, Fachgebiet fachgebiet) {

    }

}
