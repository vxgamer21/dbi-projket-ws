package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.Table;

@Table(name = "versicherungsart")
public enum Versicherungsart {
    KRANKENKASSE, PRIVAT
}
