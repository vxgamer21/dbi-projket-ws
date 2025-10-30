package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdresseConverter implements Converter<String, Adresse> {

    @Override
    public Adresse convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        String[] parts = source.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Ungültiges Adressformat: " + source);
        }
        return new Adresse(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
    }
}
