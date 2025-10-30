package com.example.zellervandecastellpatientenverwaltung.commands;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
public class ArztCommands {
    public record CreateArztCommand(
            String name,
            Fachgebiet fachgebiet,
            LocalDate geburtsdatum,
            Long svnr,
            Adresse adresse,
            TelefonNummer telefonnummer,
            Email email
    ) {}
}
