package com.example.zellervandecastellpatientenverwaltung.persistence.exception;

import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataQualityExceptionTest {

    @Test
    void when_message_is_invalid() {
        DataQualityException ex = assertThrows(DataQualityException.class, () ->
                DataQualityException.forUnsupportedEnumLiteral('I', Fachgebiet.class, "'O','H','C','G','A'")
        );

        assertEquals("Unsupported enum literal 'I' for enum class Fachgebiet in the DB, Valid values are: 'O','H','C','G','A'", ex.getMessage());
    }
}