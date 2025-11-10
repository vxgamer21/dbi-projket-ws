package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Versicherungsart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientFormDto {
    private String patientId;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate geburtsdatum;

    private Long svnr;
    private Versicherungsart versicherungsart;
    private Adresse adresse = new Adresse();
    private TelefonNummerFormDto telefonnummer = new TelefonNummerFormDto();
}
