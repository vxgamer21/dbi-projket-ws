package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailConverterTest {

    private final EmailConverter converter = new EmailConverter();

    @Test
    void testConvertToDatabaseColumn() {
        Email email = new Email("test@example.com");
        String dbColumn = converter.convertToDatabaseColumn(email);
        assertEquals("test@example.com", dbColumn);

        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void testConvertToEntityAttribute() {
        String dbData = "test@example.com";
        Email email = converter.convertToEntityAttribute(dbData);
        assertNotNull(email);
        assertEquals("test@example.com", email.getMail());

        assertNull(converter.convertToEntityAttribute(null));
    }
}