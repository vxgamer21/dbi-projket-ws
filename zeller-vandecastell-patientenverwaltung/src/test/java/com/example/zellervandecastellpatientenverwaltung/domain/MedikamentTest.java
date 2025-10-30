package com.example.zellervandecastellpatientenverwaltung.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedikamentTest {
    @Test
    void when_medikament_is_null_throws_exception(){
        Medikament.MedikamentException ex = assertThrows(Medikament.MedikamentException.class, () -> new Medikament(null,null));
    }

    @Test
    void when_medikament_name_is_null_throws_exception(){
        Medikament.MedikamentException ex = assertThrows(Medikament.MedikamentException.class, () -> new Medikament(null,"wirkstoff"));
    }

    @Test
    void when_medikament_wirkstoff_is_null_throws_exception(){
        Medikament.MedikamentException ex = assertThrows(Medikament.MedikamentException.class, () -> new Medikament("name",null));
    }


}