package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdresseConverterTest {

    private final AdresseConverter converter = new AdresseConverter();

    @Test
    void testConvertToDatabaseColumn() {
        Adresse adresse = new Adresse("Musterstraße", "10", "Musterstadt", "12345");
        String expected = "Musterstraße,Musterstadt,12345,10";
        String result = converter.convertToDatabaseColumn(adresse);
        assertEquals(expected, result, "The database column representation is incorrect.");
    }

    @Test
    void testConvertToDatabaseColumnWithNull() {
        Adresse adresse = null;
        assertNull(converter.convertToDatabaseColumn(adresse), "Null Adresse should convert to null database column.");
    }

    @Test
    void testConvertToEntityAttribute() {
        String dbData = "Musterstraße,Musterstadt,12345,10";
        Adresse expected = new Adresse("Musterstraße", "Musterstadt", "12345", "10");
        Adresse result = converter.convertToEntityAttribute(dbData);
        assertEquals(expected, result, "The entity attribute conversion is incorrect.");
    }

    @Test
    void testConvertToEntityAttributeWithNull() {
        String dbData = null;
        assertNull(converter.convertToEntityAttribute(dbData), "Null database column should convert to null Adresse.");
    }

    @Test
    void testConvertToEntityAttributeWithInvalidData() {
        String invalidData = "Musterstraße,Musterstadt,12345"; // Missing one field
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute(invalidData);
        });
        assertEquals("Invalid Adresse data: " + invalidData, exception.getMessage());
    }
}

