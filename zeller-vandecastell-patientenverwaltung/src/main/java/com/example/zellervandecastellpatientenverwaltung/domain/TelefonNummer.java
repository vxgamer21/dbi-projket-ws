package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonNummer {

    public static final int MIN_LKENNZAHL_LENGTH = 3;
    public static final int MAX_LKENNZAHL_LENGTH = 3;
    public static final int MIN_OKENNZAHL_LENGTH = 3;
    public static final int MAX_OKENNZAHL_LENGTH = 4;
    public static final int MIN_RUFNUMMER_LENGTH = 6;
    public static final int MAX_RUFNUMMER_LENGTH = 13;

    @NotNull
    @Size(min = MIN_LKENNZAHL_LENGTH, max = MAX_LKENNZAHL_LENGTH)
    private String lkennzahl;

    @NotNull
    @Size(min = MIN_OKENNZAHL_LENGTH, max = MAX_OKENNZAHL_LENGTH)
    private String okennzahl;

    @NotNull
    @Size(min = MIN_RUFNUMMER_LENGTH, max = MAX_RUFNUMMER_LENGTH)
    private String rufnummer;

    @NotNull
    private TelefonNummerArt art;

    public static class TelefonNummerException extends RuntimeException {
        private TelefonNummerException(String message) {
            super(message);
        }

        static TelefonNummerException forNullValues(String lkennzahl, String okennzahl, String rufnummer, TelefonNummerArt art) {
            return new TelefonNummerException("TelefonNummer values must not be null");
        }
    }
}
