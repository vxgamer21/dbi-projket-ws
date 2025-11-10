package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

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
    private String id;

    @NotNull(message = "Fachgebiet muss gewählt werden")
    @Field("fachgebiet")
    private Fachgebiet fachgebiet;

    @Field("email")
    @Indexed(unique = true)
    private Email email;

    @Builder.Default
    @Field("behandlungen")
    @DocumentReference(lazy = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Behandlung> behandlungen = new ArrayList<>();

    @Field("apiKey")
    private String apiKey;
}
