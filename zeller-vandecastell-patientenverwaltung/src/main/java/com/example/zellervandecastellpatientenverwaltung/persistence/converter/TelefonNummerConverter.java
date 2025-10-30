package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummerArt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TelefonNummerConverter implements Converter<String, TelefonNummerArt> {

    @Override
    public TelefonNummerArt convert(String source) {
        if (source == null) return null;
        return switch (source.toUpperCase()) {
            case "M", "MOBIL" -> TelefonNummerArt.MOBIL;
            case "B", "BUSINESS" -> TelefonNummerArt.BUSINESS;
            case "F", "FESTNETZ" -> TelefonNummerArt.FESTNETZ;
            default -> throw new IllegalArgumentException("Ungültiger TelefonNummerArt-Wert: " + source);
        };
    }
}
