package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AdresseConverter implements AttributeConverter<Adresse, String> {

    @Override
    public String convertToDatabaseColumn(Adresse adresse) {
        if (adresse == null) {
            return null;
        }
        return String.join(",", adresse.getStrasse(), adresse.getStadt(), adresse.getPlz(), adresse.getHausNr());
    }

    @Override
    public Adresse convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid Adresse data: " + dbData);
        }
        return new Adresse(parts[0], parts[1], parts[2], parts[3]);
    }



}

