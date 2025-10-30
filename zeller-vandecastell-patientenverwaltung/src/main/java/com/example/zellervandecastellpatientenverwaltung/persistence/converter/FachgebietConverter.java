package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FachgebietConverter implements Converter<String, Fachgebiet> {

    @Override
    public Fachgebiet convert(String source) {
        if (source == null || source.isBlank()) return null;
        return switch (source.trim().toUpperCase()) {
            case "O", "ORTHOPAEDIE" -> Fachgebiet.ORTHOPAEDIE;
            case "H", "HNO" -> Fachgebiet.HNO;
            case "C", "CHIRURGIE" -> Fachgebiet.CHIRURGIE;
            case "G", "GYNAEKOLOGIE" -> Fachgebiet.GYNAEKOLOGIE;
            case "A", "ALLGEMEINMEDIZIN" -> Fachgebiet.ALLGEMEINMEDIZIN;
            default -> throw new IllegalArgumentException("Ungültiges Fachgebiet: " + source);
        };
    }
}
