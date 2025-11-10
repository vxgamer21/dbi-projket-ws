package com.example.zellervandecastellpatientenverwaltung.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonNummerFormDto {
    private String landesvorwahl;
    private String ortsvorwahl;
    private String rufnummer;
    private String value;
}
