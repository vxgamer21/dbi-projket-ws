package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {

    @NotNull
    private String mail;

    public void setMail(String mail) {
        if (mail == null) {
            throw EmailException.forNullValue();
        }
        if (!mail.contains("@") || !mail.contains(".") || mail.length() < 5 || mail.length() > 50 || mail.contains(" ")) {
            throw EmailException.forInvalidValue();
        }
        this.mail = mail;
    }

    public static class EmailException extends RuntimeException {
        private EmailException(String message) {
            super(message);
        }

        public static EmailException forNullValue() {
            return new EmailException("Email values must not be null");
        }

        public static EmailException forInvalidValue() {
            return new EmailException("Email values must be valid");
        }
    }
}
