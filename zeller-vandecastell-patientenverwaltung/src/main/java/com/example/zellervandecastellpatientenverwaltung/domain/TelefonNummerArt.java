package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.Table;

@Table(name = "telefonnummerart")
public enum TelefonNummerArt {
    MOBIL, BUSINESS, FESTNETZ
}
