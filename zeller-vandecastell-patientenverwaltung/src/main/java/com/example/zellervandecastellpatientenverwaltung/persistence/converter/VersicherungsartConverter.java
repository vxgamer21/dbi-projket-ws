package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import com.example.zellervandecastellpatientenverwaltung.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VersicherungsartConverter implements AttributeConverter<Versicherungsart, Character> {

    private static final String VALID_VALUES = "'K','P'";
    public static final String COLUMN_DEFINITION = "enum(" + VALID_VALUES + ")";

    @Override
    public Character convertToDatabaseColumn(Versicherungsart versicherungsart) {
        return switch (versicherungsart) {
            case KRANKENKASSE -> 'K';
            case PRIVAT -> 'P';
            case null -> null;
        };
    }

    @Override
    public Versicherungsart convertToEntityAttribute(Character s) {
        return switch (s) {
            case 'K' -> Versicherungsart.KRANKENKASSE;
            case 'P' -> Versicherungsart.PRIVAT;
            case null -> null;
            default -> throw DataQualityException.forUnsupportedEnumLiteral(s, Versicherungsart.class, VALID_VALUES);
        };
    }
}