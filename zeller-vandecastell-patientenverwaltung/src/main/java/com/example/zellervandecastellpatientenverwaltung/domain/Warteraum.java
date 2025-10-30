package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("W")
@Table(name = "warteraum")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Warteraum extends Raum{
    @EmbeddedId
    private WarteraumId warteraumId;

    @Min(value = 0, message = "Es gibt keine negative Anazhl an Sitzplätzen")
    private int anzahlSitzplaetze;

    @NotNull
    private String apiKey;

    @Embeddable
    public record WarteraumId(@GeneratedValue @NotNull Long id) {}
}
