package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;

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

        public static MedikamentException forNullValue() {
            return new MedikamentException("Medikament values must not be null");
        }
    }
}
