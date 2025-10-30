package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import com.example.zellervandecastellpatientenverwaltung.domain.TelefonNummer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArztFormDto {
    private Long arztid;
    private String name;
    private LocalDate geburtsdatum;
    private Long svnr;
    private Fachgebiet fachgebiet;
    private Adresse adresse = new Adresse();
    private TelefonNummer telefonnummer = new TelefonNummer();
    private Email email = new Email();
}
