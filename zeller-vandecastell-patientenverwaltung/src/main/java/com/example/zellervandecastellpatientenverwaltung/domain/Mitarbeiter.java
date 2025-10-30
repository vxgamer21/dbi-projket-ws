package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.Max;
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
@Document(collection = "mitarbeiter")
public class Mitarbeiter extends User {

    @Id
    private String id;

    @Min(value = 0, message = "Es gibt kein negatives Gehalt")
    @Max(value = 100000, message = "Das Gehalt ist zu hoch")
    @Field("gehalt")
    private Long gehalt;

    @NotNull
    @Field("email")
    private Email email;

    @Field("arztpraxisId")
    private String arztpraxisId;

    @NotNull
    @Field("apiKey")
    private String apiKey;
}
