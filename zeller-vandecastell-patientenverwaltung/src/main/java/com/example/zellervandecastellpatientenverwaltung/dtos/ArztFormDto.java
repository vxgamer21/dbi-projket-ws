package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Adresse;
import com.example.zellervandecastellpatientenverwaltung.domain.Email;
import com.example.zellervandecastellpatientenverwaltung.domain.Fachgebiet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArztFormDto {
    private String arztid;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate geburtsdatum;

    private Long svnr;
    private Fachgebiet fachgebiet;
    private Adresse adresse = new Adresse();
    private TelefonNummerFormDto telefonnummer = new TelefonNummerFormDto();
    private Email email = new Email();
}
