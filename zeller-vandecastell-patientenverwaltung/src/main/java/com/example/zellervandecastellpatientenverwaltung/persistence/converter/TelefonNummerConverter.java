package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummerArt;
import com.example.zellervandecastellpatientenverwaltung.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TelefonNummerConverter implements AttributeConverter<TelefonNummerArt, Character> {

    private static final String VALID_VALUES = "'M','B','F'";
    public static final String COLUMN_DEFINITION = "enum(" + VALID_VALUES + ")";

    @Override
    public Character convertToDatabaseColumn(TelefonNummerArt telefonNummer) {
        return switch (telefonNummer) {
            case MOBIL -> 'M';
            case BUSINESS -> 'B';
            case FESTNETZ -> 'F';
            case null -> null;
        };
    }

    @Override
    public TelefonNummerArt convertToEntityAttribute(Character s) {
        return switch (s) {
            case 'M' -> TelefonNummerArt.MOBIL;
            case 'B' -> TelefonNummerArt.BUSINESS;
            case 'F' -> TelefonNummerArt.FESTNETZ;
            case null -> null;
            default -> throw DataQualityException.forUnsupportedEnumLiteral(s, TelefonNummerArt.class, VALID_VALUES);
        };
    }
}