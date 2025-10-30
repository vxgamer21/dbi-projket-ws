package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("B")
@Table(name = "behandlungsraum")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Behandlungsraum extends Raum {
    @EmbeddedId
    private BehandlungsraumId behandlungsraumId;

    private String ausstattung;

    private Boolean isFrei;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Behandlung> behandlungen;

    @NotNull
    private String apiKey;

    @Embeddable
    public record BehandlungsraumId(@GeneratedValue @NotNull Long id) {}
}