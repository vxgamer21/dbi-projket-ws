package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
public record Medikament(@NotNull String name, @NotNull String wirkstoff) {
    public Medikament {
        if (name == null || wirkstoff == null) {
            throw MedikamentException.forNullValue();
        }
    }

    public static class MedikamentException extends RuntimeException {
        private MedikamentException(String message) {
            super(message);
        }

        static MedikamentException forNullValue() {
            return new MedikamentException("Medikament values must not be null");
        }
    }
}