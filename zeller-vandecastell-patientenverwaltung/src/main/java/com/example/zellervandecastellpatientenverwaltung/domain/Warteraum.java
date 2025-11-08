package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Warteraum extends Raum {


    @Min(value = 0, message = "Es gibt keine negative Anzahl an Sitzplätzen")
    @Field("anzahlSitzplaetze")
    private int anzahlSitzplaetze;

    @NotNull
    @Field("apiKey")
    private String apiKey;
}
