package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@NoArgsConstructor
@Builder

@Embeddable
@Table(name = "adresse")
public class Email {

    @NotNull
    private String mail;


    public String getMail() {
        return mail;
    }

    public Email(String mail) {
        setMail(mail);
    }


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
