package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmailConverter implements Converter<String, Email> {

    @Override
    public Email convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return new Email(source.trim());
    }
}
