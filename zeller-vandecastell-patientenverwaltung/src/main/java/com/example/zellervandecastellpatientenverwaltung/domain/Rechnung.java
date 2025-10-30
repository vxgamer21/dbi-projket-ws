package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "rechnungen")
public class Rechnung {

    @Id
    private String id;

    @NotNull
    @Field("patientId")
    private String patientId;

    @NotNull
    @Field("arztId")
    private String arztId;

    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "10000.0", inclusive = true)
    @NotNull
    @Field("betrag")
    private Double betrag;

    @Field("bezahlt")
    private boolean bezahlt;

    @Field("zahlungsart")
    private Zahlungsart zahlungsart;

    @Field("datum")
    private LocalDateTime datum;

    @NotNull
    @Field("apiKey")
    private String apiKey;
}
