package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@DiscriminatorValue("P")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "patient")
public class Patient extends User {

    @EmbeddedId
    private PatientID patientID;

    @Column(columnDefinition = "enum ('K','P')")
    public Versicherungsart versicherungsart;


    private String apiKey;

    @Embeddable
    public record PatientID(@GeneratedValue @NotNull Long id) {}

    public Long getId() {
        return patientID != null ? patientID.id() : null;
    }

}