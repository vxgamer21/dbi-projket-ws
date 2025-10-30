package com.example.zellervandecastellpatientenverwaltung.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class User implements Serializable {

    @NotBlank(message = "Name darf nicht leer sein")
    public String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate gebDatum;

    @NotNull(message = "SVNR darf nicht leer sein")
    private Long svnr;
    private Adresse adresse;
    @Embedded
    private TelefonNummer telefonNummer;
}
