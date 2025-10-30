package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.Table;

@Table(name = "fachgebiet")
public enum Fachgebiet {
    ORTHOPAEDIE, HNO, CHIRURGIE, GYNAEKOLOGIE, ALLGEMEINMEDIZIN
}
