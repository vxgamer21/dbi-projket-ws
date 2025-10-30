package com.example.zellervandecastellpatientenverwaltung.presentation.www;

import com.example.zellervandecastellpatientenverwaltung.persistence.converter.AdressConverterWeb;
import com.example.zellervandecastellpatientenverwaltung.persistence.converter.EmailConverterWeb;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new AdressConverterWeb());
        registry.addConverter(new EmailConverterWeb());
    }
}
