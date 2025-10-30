package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "arztpraxen")
public class Arztpraxis {

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("istKassenarzt")
    private Boolean istKassenarzt;

    @Field("adresse")
    private Adresse adresse;

    @Field("telefonNummer")
    private TelefonNummer telefonNummer;

    @Field("aerzte")
    private List<Arzt> aerzte;

    @Field("mitarbeiter")
    private List<Mitarbeiter> mitarbeiter;

    @Field("behandlungsraeume")
    private List<Behandlungsraum> behandlungsraum;

    @Field("warteraeume")
    private List<Warteraum> warteraum;

    @NotNull
    @Field("apiKey")
    private String apiKey;
}
