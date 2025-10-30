package com.example.zellervandecastellpatientenverwaltung.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    @Test
    void when_email_is_null_throws_exception(){
        Email.EmailException ex = assertThrows(Email.EmailException.class, () -> new Email(null));
        assertThat(ex.getMessage()).isEqualTo("Email values must not be null");
    }

    @Test
    void when_email_is_invalid(){
        assertThatThrownBy(() -> new Email("test"))
                .isInstanceOf(Email.EmailException.class)
                .hasMessage("Email values must be valid");
    }

}