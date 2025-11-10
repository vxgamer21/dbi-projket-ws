package com.example.zellervandecastellpatientenverwaltung.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "patienten")
@TypeAlias("Patient")
public class Patient extends User {

    @Id
    private String id;

    @Field("versicherungsart")
    private Versicherungsart versicherungsart;

    @Builder.Default
    @Field("behandlungen")
    @DocumentReference(lazy = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Behandlung> behandlungen = new ArrayList<>();

    @Field("apiKey")
    private String apiKey;
}
