package com.example.zellervandecastellpatientenverwaltung.domain;

import com.example.zellervandecastellpatientenverwaltung.foundation.ApiKeyGenerator;

import java.time.LocalDate;
import java.util.List;

public class FixturesFactory {

    public static Adresse Ringstrasse() {
        return Adresse.builder()
                .strasse("Ringstraße")
                .hausNr("1")
                .plz("1010")
                .stadt("Wien")
                .build();
    }

    public static TelefonNummer StandardMobil() {
        return new TelefonNummer("+43", "664", "1234567", TelefonNummerArt.MOBIL);
    }

    public static Behandlungsraum BehandlungsRaum1Frei() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Behandlungsraum.builder()
                .name("Raum 1")
                .isFrei(true)
                .behandlungen(null)
                .apiKey(apiKey)
                .build();
    }

    public static Warteraum WarteRaum22Seats() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Warteraum.builder()
                .name("Warteraum 1")
                .anzahlSitzplaetze(22)
                .apiKey(apiKey)
                .build();
    }

    public static Arzt ArztAllgemein() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Arzt.builder()
                .name("Dr. Allgemein")
                .gebDatum(null)
                .svnr(1234567890L)
                .adresse(Ringstrasse())
                .telefonNummer(StandardMobil())
                .apiKey(apiKey)
                .fachgebiet(Fachgebiet.ALLGEMEINMEDIZIN)
                .build();
    }

    public static Patient PatientMaxMustermann() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Patient.builder()
                .name("Max Mustermann")
                .gebDatum(null)
                .svnr(1234567890L)
                .adresse(Ringstrasse())
                .telefonNummer(StandardMobil())
                .apiKey(apiKey)
                .build();
    }

    public static Behandlung Behandlung1() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Behandlung.builder()
                .behandlungsraumId("1")
                .patientId("2")
                .arztId("3")
                .apiKey(apiKey)
                .build();
    }

    public static Mitarbeiter MitarbeiterMaxMuster() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Mitarbeiter.builder()
                .name("Max Muster")
                .svnr(1234567890L)
                .gehalt(2000L)
                .email(new Email("max@muster.at"))
                .gebDatum(LocalDate.of(1970, 1, 1))
                .adresse(Ringstrasse())
                .telefonNummer(StandardMobil())
                .apiKey(apiKey)
                .build();
    }

    public static Arztpraxis ArztpraxisStandard() {
        String apiKey = ApiKeyGenerator.generateApiKey();

        return Arztpraxis.builder()
                .name("Ordination Dr. Müller")
                .istKassenarzt(true)
                .adresse(new Adresse("Hauptstraße", "10", "1010", "Wien"))
                .telefonNummer(StandardMobil())
                .aerzte(List.of(ArztAllgemein()))
                .mitarbeiter(List.of(MitarbeiterMaxMuster()))
                .behandlungsraum(List.of(BehandlungsRaum1Frei()))
                .warteraum(List.of(WarteRaum22Seats()))
                .apiKey(apiKey)
                .build();
    }
}
