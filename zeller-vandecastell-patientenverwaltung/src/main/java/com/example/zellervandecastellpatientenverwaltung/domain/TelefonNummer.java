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

    /**
     * Parst eine Telefonnummer aus einem String im Format +43 660 1234567 oder ähnlich
     */
    public static TelefonNummer fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        // Entferne alle Leerzeichen und nicht-numerischen Zeichen außer +
        String cleaned = value.replaceAll("[\\s-()]", "");

        // Format: +43 660 1234567 -> lkennzahl=+43, okennzahl=660, rufnummer=1234567
        if (cleaned.startsWith("+")) {
            // Extrahiere Länderkennzahl (z.B. +43)
            int firstNonDigitAfterPlus = 2;
            while (firstNonDigitAfterPlus < cleaned.length() &&
                   Character.isDigit(cleaned.charAt(firstNonDigitAfterPlus))) {
                firstNonDigitAfterPlus++;
            }

            String lkennzahl = cleaned.substring(0, Math.min(firstNonDigitAfterPlus, 4));
            String rest = cleaned.substring(lkennzahl.length());

            // Extrahiere Ortskennzahl (3-4 Ziffern)
            String okennzahl = rest.length() >= 3 ? rest.substring(0, Math.min(4, rest.length())) : rest;
            String rufnummer = rest.length() > okennzahl.length() ? rest.substring(okennzahl.length()) : "";

            if (rufnummer.isEmpty() && rest.length() > 3) {
                okennzahl = rest.substring(0, 3);
                rufnummer = rest.substring(3);
            }

            return new TelefonNummer(lkennzahl, okennzahl, rufnummer, TelefonNummerArt.MOBIL);
        }

        return null;
    }

    /**
     * Gibt die Telefonnummer als formatierten String zurück
     */
    public String toFormattedString() {
        if (lkennzahl == null || okennzahl == null || rufnummer == null) {
            return "";
        }
        return lkennzahl + " " + okennzahl + " " + rufnummer;
    }

    public static class TelefonNummerException extends RuntimeException {
        private TelefonNummerException(String message) {
            super(message);
        }

        static TelefonNummerException forNullValues(String lkennzahl, String okennzahl, String rufnummer, TelefonNummerArt art) {
            return new TelefonNummerException("TelefonNummer values must not be null");
        }
    }
}
