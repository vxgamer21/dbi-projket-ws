package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Behandlungsraum extends Raum {


    @Field("ausstattung")
    private String ausstattung;

    @Field("isFrei")
    private Boolean isFrei;

    @Field("behandlungen")
    private List<Behandlung> behandlungen;

    @NotNull
    @Field("apiKey")
    private String apiKey;
}
