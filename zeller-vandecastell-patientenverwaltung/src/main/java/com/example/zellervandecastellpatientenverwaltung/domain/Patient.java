package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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


    @Field("apiKey")
    private String apiKey;
}
