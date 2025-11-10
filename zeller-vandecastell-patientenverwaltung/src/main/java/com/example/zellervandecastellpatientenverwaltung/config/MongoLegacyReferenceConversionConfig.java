package com.example.zellervandecastellpatientenverwaltung.config;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoLegacyReferenceConversionConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(new ObjectIdToBehandlungConverter()));
    }

    @ReadingConverter
    static class ObjectIdToBehandlungConverter implements Converter<ObjectId, Behandlung> {

        @Override
        public Behandlung convert(ObjectId source) {
            if (source == null) {
                return null;
            }

            Behandlung behandlung = new Behandlung();
            behandlung.setId(source.toHexString());
            return behandlung;
        }
    }
}

