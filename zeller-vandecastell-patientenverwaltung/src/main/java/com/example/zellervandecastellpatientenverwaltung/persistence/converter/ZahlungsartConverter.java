package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.domain.Zahlungsart;
import com.example.zellervandecastellpatientenverwaltung.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ZahlungsartConverter implements AttributeConverter<Zahlungsart, Character> {

    private static final String VALID_VALUES = "'K','B','R'";
    public static final String COLUMN_DEFINITION = "enum(" + VALID_VALUES + ")";

    @Override
    public Character convertToDatabaseColumn(Zahlungsart zahlungsart) {
        return switch (zahlungsart) {
            case BARZAHLUNG -> 'B';
            case KREDITKARTE -> 'K';
            case RECHNUNG -> 'R';


            case null -> null;
        };
    }

    @Override
    public Zahlungsart convertToEntityAttribute(Character s) {
        return switch (s) {
            case 'B' -> Zahlungsart.BARZAHLUNG;
            case 'K' -> Zahlungsart.KREDITKARTE;
            case 'R' -> Zahlungsart.RECHNUNG;
            case null -> null;
            default -> throw DataQualityException.forUnsupportedEnumLiteral(s, Fachgebiet.class, VALID_VALUES);
        };
    }
}
