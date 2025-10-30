package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Arzt-Dokument für MongoDB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "aerzte")
public class Arzt extends User {

    @Id
    private String id; // Mongo ObjectId (automatisch generiert)

    @NotNull(message = "Fachgebiet muss gewählt werden")
    @Field("fachgebiet")
    private Fachgebiet fachgebiet;

    @Field("email")
    @Indexed(unique = true)
    private Email email;

    @Field("apiKey")
    private String apiKey;
}
