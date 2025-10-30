package com.example.zellervandecastellpatientenverwaltung.domain;

import com.example.zellervandecastellpatientenverwaltung.persistence.converter.EmailConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("M")
@Table(name = "mitarbeiter")
public class Mitarbeiter extends User {

    @EmbeddedId
    private MitarbeiterID mitarbeiterID;

    @Min(value = 0, message = "Es gibt kein negatives Gehalt")
    @Max(value = 100000, message = "Das Gehalt ist zu hoch")
    private Long gehalt;

    @Convert(converter = EmailConverter.class)
    @NotNull
    private Email email;

    @ManyToOne
    @JoinColumn(name = "arztpraxis_id")
    private Arztpraxis arztpraxis;

    @NotNull
    private String apiKey;

    @Embeddable
    public record MitarbeiterID(@GeneratedValue @NotNull Long id) {}
}