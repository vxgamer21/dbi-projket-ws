package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummerArt;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TelefonNummerConverterTest {

    private TelefonNummerConverter converter = new TelefonNummerConverter();

    @Nested
    class convert_enum_instance_to_db_value {
        @Test
        void can_convert_enum_instance() {
            // Assert
            assertThat(converter.convertToDatabaseColumn(TelefonNummerArt.MOBIL)).isEqualTo('M');
            assertThat(converter.convertToDatabaseColumn(TelefonNummerArt.BUSINESS)).isEqualTo('B');
            assertThat(converter.convertToDatabaseColumn(TelefonNummerArt.FESTNETZ)).isEqualTo('F');
        }

        @Test
        void can_convert_null() {
            // Assert
            assertThat(converter.convertToDatabaseColumn(null)).isNull();
        }
    }

    @Nested
    class convert_db_value_to_enum_instance {

        @ParameterizedTest
        @MethodSource
        void can_convert_db_value_when_valid_literal_is_provided(Character literal, TelefonNummerArt telefonNummer) {
            assertThat(converter.convertToEntityAttribute(literal)).isEqualTo(telefonNummer);
        }

        static Stream<Arguments> can_convert_db_value_when_valid_literal_is_provided() {
            return Stream.of(
                    Arguments.of('M', TelefonNummerArt.MOBIL),
                    Arguments.of('B', TelefonNummerArt.BUSINESS),
                    Arguments.of('F', TelefonNummerArt.FESTNETZ)
            );
        }

        @Test
        void can_convert_null() {
            // Assert
            assertThat(converter.convertToEntityAttribute(null)).isNull();
        }
    }
}
