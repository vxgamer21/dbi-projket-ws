package com.example.zellervandecastellpatientenverwaltung.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BehandlungFormDto {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime beginn;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ende;

    private String diagnose;

    @NotNull
    private String arztId;

    @NotNull
    private String patientId;

    private String id;

    public String getBeginnFormatted() {
        return beginn != null ? beginn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }

    public String getEndeFormatted() {
        return ende != null ? ende.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }

}
