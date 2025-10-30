package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VersicherungsartConverter implements Converter<String, Versicherungsart> {

    @Override
    public Versicherungsart convert(String source) {
        if (source == null || source.isBlank()) return null;
        return switch (source.trim().toUpperCase()) {
            case "K", "KRANKENKASSE" -> Versicherungsart.KRANKENKASSE;
            case "P", "PRIVAT" -> Versicherungsart.PRIVAT;
            default -> throw new IllegalArgumentException("Ungültige Versicherungsart: " + source);
        };
    }
}
