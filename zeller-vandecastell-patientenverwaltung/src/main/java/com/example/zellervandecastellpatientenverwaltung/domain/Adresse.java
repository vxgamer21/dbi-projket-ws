package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
@Table(name = "adresse")
public class Adresse {

    @Column(length = 64)
    private String strasse;
    @Column(length = 16)
    private String hausNr;
    @Column(length = 64)
    private String stadt;
    @Column(length = 16)
    private String plz;

    @PostConstruct
    public void validate() {
        if (strasse == null || stadt == null || plz == null || hausNr == null) {
            throw new AdresseException("Alle Felder müssen gesetzt sein.");
        }
    }

    public static class AdresseException extends RuntimeException {
        public AdresseException(String message) {
            super(message);
        }
    }
}
