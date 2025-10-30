package com.example.zellervandecastellpatientenverwaltung.domain;

import com.example.zellervandecastellpatientenverwaltung.assertions.AdresseAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class AdresseTest {

    private Adresse adresse;

    @BeforeEach
    void setUp() {
        adresse = new Adresse("Hauptstraße", "1", "Baden", "2500");
    }

    @Test
    void when_strasse_is_valid() {
//        // assertEquals("Adressen values must not be null", ex.getMessage());
//        assertThat(ex.getMessage()).isEqualTo("Adressen values must not be null");
        AdresseAssert.assertThat(adresse).hasStrasse("Hauptstraße");
    }

    @Test
    void when_stadt_is_valid() {
        AdresseAssert.assertThat(adresse).hasStadt("Baden");
    }

    @Test
    void when_plz_is_valid() {
        AdresseAssert.assertThat(adresse).hasPlz("2500");
    }

    @Test
    void when_hausNr_is_valid() {
        AdresseAssert.assertThat(adresse).hasHausNr("1");
    }




//    @Test
//    void when_hausNr_is_null_throws_exception() {
//        Adresse.AdresseException ex = assertThrows(Adresse.AdresseException.class, () -> new Adresse("Hauptstraße", "Baden", "2500", null));
//        assertEquals("Adressen values must not be null", ex.getMessage());
//    }

    @Test
    void when_all_values_are_valid_creates_adresse() {
        Adresse adresse = new Adresse("Hauptstraße", "1", "Baden", "2500");
        assertNotNull(adresse);
        assertEquals("Hauptstraße", adresse.getStrasse());
        assertEquals("Baden", adresse.getStadt());
        assertEquals("2500", adresse.getPlz());
        assertEquals("1", adresse.getHausNr());
    }
}