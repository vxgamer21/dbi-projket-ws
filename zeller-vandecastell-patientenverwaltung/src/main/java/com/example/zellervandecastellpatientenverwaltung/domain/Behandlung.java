package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "behandlungen")
public class Behandlung {

    @Id
    private String id;

    @NotNull
    @Field("arzt")
    private Arzt arzt;

    @NotNull
    @Field("patient")
    private Patient patient;

    @Field("behandlungsraumId")
    private String behandlungsraumId;

    @Field("medikamente")
    private List<Medikament> medikamente;

    @Field("beginn")
    private LocalDateTime beginn;

    @Field("ende")
    private LocalDateTime ende;

    @Field("diagnose")
    private String diagnose;

    @Field("apiKey")
    private String apiKey;
}
