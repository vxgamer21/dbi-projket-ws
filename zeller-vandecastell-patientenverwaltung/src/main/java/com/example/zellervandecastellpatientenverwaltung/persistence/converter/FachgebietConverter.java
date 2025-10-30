package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FachgebietConverter implements AttributeConverter<Fachgebiet, Character> {

    private static final String VALID_VALUES = "'O','H','C','G','A'";
    public static final String COLUMN_DEFINITION = "enum(" + VALID_VALUES + ")";

    @Override
    public Character convertToDatabaseColumn(Fachgebiet fachgebiet) {
        return switch (fachgebiet) {
            case ORTHOPAEDIE -> 'O';
            case HNO -> 'H';
            case CHIRURGIE -> 'C';
            case GYNAEKOLOGIE -> 'G';
            case ALLGEMEINMEDIZIN -> 'A';

            case null -> null;
        };
    }

    @Override
    public Fachgebiet convertToEntityAttribute(Character s) {
        return switch (s) {
            case 'O','o' -> Fachgebiet.ORTHOPAEDIE;
            case 'H','h' -> Fachgebiet.HNO;
            case 'C','c' -> Fachgebiet.CHIRURGIE;
            case 'G','g' -> Fachgebiet.GYNAEKOLOGIE;
            case 'A','a' -> Fachgebiet.ALLGEMEINMEDIZIN;

            case null -> null;
            default -> throw DataQualityException.forUnsupportedEnumLiteral(s, Fachgebiet.class, VALID_VALUES);
        };
    }
}
