package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BehandlungFormDto {
    @NotNull
    private LocalDateTime beginn;

    private LocalDateTime ende;


    private String diagnose;

    @NotNull
    private Long arztId;

    @NotNull
    private Long patientId;

    private Long behandlungId;

    public String getBeginnFormatted() {
        return beginn != null ? beginn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }

    public String getEndeFormatted() {
        return ende != null ? ende.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }


}
