package com.example.zellervandecastellpatientenverwaltung.domain;

import com.example.zellervandecastellpatientenverwaltung.persistence.converter.EmailConverter;
import com.example.zellervandecastellpatientenverwaltung.persistence.converter.FachgebietConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("A")
@Table(name = "arzt")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Arzt extends User {

    @EmbeddedId
    private ArztId arztId;

    @NotNull(message = "Fachgebiet muss gewählt werden")
    @Convert(converter = FachgebietConverter.class)
    @Column(columnDefinition = FachgebietConverter.COLUMN_DEFINITION)
    public Fachgebiet fachgebiet;

    @Convert(converter = EmailConverter.class)
    private Email email;


    private String apiKey;

    @Embeddable
    public record ArztId(@GeneratedValue @NotNull Long id) {}

    public Long getId() {
        return arztId != null ? arztId.id() : null;
    }
}