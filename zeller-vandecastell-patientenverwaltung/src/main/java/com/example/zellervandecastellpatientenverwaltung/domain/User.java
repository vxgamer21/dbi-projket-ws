package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class User implements Serializable {

    @NotBlank(message = "Name darf nicht leer sein")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate gebDatum;

    @NotNull(message = "SVNR darf nicht leer sein")
    private Long svnr;

    private Adresse adresse;

    private TelefonNummer telefonNummer;
}
