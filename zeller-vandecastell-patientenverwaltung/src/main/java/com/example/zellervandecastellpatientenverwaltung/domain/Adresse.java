package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adresse {

    @Field("strasse")
    @NotBlank
    @Size(max = 64)
    private String strasse;

    @Field("hausNr")
    @NotBlank
    @Size(max = 16)
    private String hausNr;

    @Field("stadt")
    @NotBlank
    @Size(max = 64)
    private String stadt;

    @Field("plz")
    @NotBlank
    @Size(max = 16)
    private String plz;

    // Optional: statische Validierungsmethode, kein Lifecycle-Callback nötig
    public void validate() {
        if (strasse == null || hausNr == null || stadt == null || plz == null) {
            throw new AdresseException("Alle Felder müssen gesetzt sein.");
        }
    }

    public static class AdresseException extends RuntimeException {
        public AdresseException(String message) {
            super(message);
        }
    }
}
