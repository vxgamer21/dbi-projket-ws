package com.example.zellervandecastellpatientenverwaltung.persistence.converter;

import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FachgebietConverterTest {

    private FachgebietConverter converter = new FachgebietConverter();

    @Nested
    class convert_enum_instance_to_db_value {
        @Test
        void can_convert_enum_instance() {
            // Assert
            assertThat(converter.convertToDatabaseColumn(Fachgebiet.ALLGEMEINMEDIZIN)).isEqualTo('A');
            assertThat(converter.convertToDatabaseColumn(Fachgebiet.CHIRURGIE)).isEqualTo('C');
            assertThat(converter.convertToDatabaseColumn(Fachgebiet.GYNAEKOLOGIE)).isEqualTo('G');
            assertThat(converter.convertToDatabaseColumn(Fachgebiet.HNO)).isEqualTo('H');
            assertThat(converter.convertToDatabaseColumn(Fachgebiet.ORTHOPAEDIE)).isEqualTo('O');
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
        void can_convert_db_value_when_valid_literal_is_provided(Character literal, Fachgebiet fachgebiet) {
            assertThat(converter.convertToEntityAttribute(literal)).isEqualTo(fachgebiet);
        }

        static Stream<Arguments> can_convert_db_value_when_valid_literal_is_provided() {
            return Stream.of(
                    Arguments.of('A', Fachgebiet.ALLGEMEINMEDIZIN),
                    Arguments.of('C', Fachgebiet.CHIRURGIE),
                    Arguments.of('G', Fachgebiet.GYNAEKOLOGIE),
                    Arguments.of('H', Fachgebiet.HNO),
                    Arguments.of('O', Fachgebiet.ORTHOPAEDIE)
            );
        }

        @Test
        void can_convert_null() {
            // Assert
            assertThat(converter.convertToEntityAttribute(null)).isNull();
        }
    }
}
