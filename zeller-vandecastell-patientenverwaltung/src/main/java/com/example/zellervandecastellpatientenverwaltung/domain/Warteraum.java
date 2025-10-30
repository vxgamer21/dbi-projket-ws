package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "warteraeume")
public class Warteraum extends Raum {

    @Id
    private String id;

    @Min(value = 0, message = "Es gibt keine negative Anzahl an Sitzplätzen")
    @Field("anzahlSitzplaetze")
    private int anzahlSitzplaetze;

    @NotNull
    @Field("apiKey")
    private String apiKey;
}
