package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdressConverterWeb implements Converter<String[], Adresse> {

    @Override
    public Adresse convert(String[] source) {
        if (source.length < 4) {
            throw new IllegalArgumentException("Invalid Adresse data");
        }
        return new Adresse(source[0], source[1], source[2], source[3]);
    }
}
