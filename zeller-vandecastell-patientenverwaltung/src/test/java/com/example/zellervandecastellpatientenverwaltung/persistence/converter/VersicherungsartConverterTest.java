package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VersicherungsartConverterTest {

    private VersicherungsartConverter converter = new VersicherungsartConverter();

    @Nested
    class convert_enum_instance_to_db_value {
        @Test
        void can_convert_enum_instance() {
            // Assert
            assertThat(converter.convertToDatabaseColumn(Versicherungsart.KRANKENKASSE)).isEqualTo('K');
            assertThat(converter.convertToDatabaseColumn(Versicherungsart.PRIVAT)).isEqualTo('P');
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
        void can_convert_db_value_when_valid_literal_is_provided(Character literal, Versicherungsart versicherungsart) {
            assertThat(converter.convertToEntityAttribute(literal)).isEqualTo(versicherungsart);
        }

        static Stream<Arguments> can_convert_db_value_when_valid_literal_is_provided() {
            return Stream.of(
                    Arguments.of('K', Versicherungsart.KRANKENKASSE),
                    Arguments.of('P', Versicherungsart.PRIVAT)
            );
        }

        @Test
        void can_convert_null() {
            // Assert
            assertThat(converter.convertToEntityAttribute(null)).isNull();
        }
    }
}
