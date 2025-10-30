package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class EmailConverterWeb implements Converter<String, Email> {

    @Override
    public Email convert(String source) {
        return new Email(source);
    }
}
