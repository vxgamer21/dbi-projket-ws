package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.Optional;


@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email,String> {
    @Override
    public String convertToDatabaseColumn(Email email) {
        return Optional.ofNullable(email)
                .map(Email::getMail)
                .filter(Objects::nonNull)
                .orElse(null);
    }

    @Override
    public Email convertToEntityAttribute(String s) {
        return switch (s){
            case null -> null;
            default -> new Email(s);
        };
    }
}
