package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Zahlungsart;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ZahlungsartConverter implements Converter<String, Zahlungsart> {

    @Override
    public Zahlungsart convert(String source) {
        if (source == null || source.isBlank()) return null;
        return switch (source.trim().toUpperCase()) {
            case "B", "BARZAHLUNG" -> Zahlungsart.BARZAHLUNG;
            case "K", "KREDITKARTE" -> Zahlungsart.KREDITKARTE;
            case "R", "RECHNUNG" -> Zahlungsart.RECHNUNG;
            default -> throw new IllegalArgumentException("Ungültige Zahlungsart: " + source);
        };
    }
}
