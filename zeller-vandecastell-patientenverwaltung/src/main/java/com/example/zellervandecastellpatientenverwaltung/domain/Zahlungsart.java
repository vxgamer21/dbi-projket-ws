package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.Table;

@Table(name = "zahlungsart")
public enum Zahlungsart {
    BARZAHLUNG,
    KREDITKARTE,
    RECHNUNG
}
