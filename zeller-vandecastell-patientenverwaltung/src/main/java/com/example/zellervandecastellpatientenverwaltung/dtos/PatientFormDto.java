package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientFormDto {
    private Long patientId;
    private String name;
    private LocalDate geburtsdatum;
    private Long svnr;
    private Versicherungsart versicherungsart;
    private Adresse adresse = new Adresse();
    private TelefonNummer telefonnummer = new TelefonNummer();

}
