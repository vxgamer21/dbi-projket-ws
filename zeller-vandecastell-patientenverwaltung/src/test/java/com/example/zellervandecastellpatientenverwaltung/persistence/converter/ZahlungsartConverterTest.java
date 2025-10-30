package com.example.zellervandecastellpatientenverwaltung.persistence.converter;


import com.example.zellervandecastellpatientenverwaltung.domain.Zahlungsart;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ZahlungsartConverterTest {

    private ZahlungsartConverter converter = new ZahlungsartConverter();

    @Nested
    class convert_enum_instance_to_db_value {
        @Test
        void can_convert_enum_instance() {
            // Assert
            assertThat(converter.convertToDatabaseColumn(Zahlungsart.BARZAHLUNG)).isEqualTo('B');
            assertThat(converter.convertToDatabaseColumn(Zahlungsart.KREDITKARTE)).isEqualTo('K');
            assertThat(converter.convertToDatabaseColumn(Zahlungsart.RECHNUNG)).isEqualTo('R');

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
        void can_convert_db_value_when_valid_literal_is_provided(Character literal, Zahlungsart zahlungsart) {
            assertThat(converter.convertToEntityAttribute(literal)).isEqualTo(zahlungsart);
        }

        static Stream<Arguments> can_convert_db_value_when_valid_literal_is_provided() {
            return Stream.of(
                    Arguments.of('B', Zahlungsart.BARZAHLUNG),
                    Arguments.of('K', Zahlungsart.KREDITKARTE),
                    Arguments.of('R', Zahlungsart.RECHNUNG)

            );
        }

        @Test
        void can_convert_null() {
            assertThat(converter.convertToEntityAttribute(null)).isNull();
        }
    }
}
