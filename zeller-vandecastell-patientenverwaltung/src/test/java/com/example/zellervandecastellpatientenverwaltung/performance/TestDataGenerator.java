package com.example.zellervandecastellpatientenverwaltung.performance;

import com.example.zellervandecastellpatientenverwaltung.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class TestDataGenerator {

    private static final Random random = new Random();

    private static final String[] VORNAMEN = {
        "Max", "Anna", "Thomas", "Julia", "Michael", "Sarah", "Christian", "Laura",
        "Andreas", "Sophie", "Stefan", "Lisa", "Martin", "Emma", "Daniel", "Marie"
    };

    private static final String[] NACHNAMEN = {
        "Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner",
        "Becker", "Schulz", "Hoffmann", "Koch", "Bauer", "Richter", "Klein", "Wolf"
    };

    private static final String[] STRASSEN = {
        "Hauptstraße", "Bahnhofstraße", "Kirchstraße", "Dorfstraße", "Gartenweg",
        "Waldstraße", "Ringstraße", "Bergstraße", "Seestraße", "Parkweg"
    };

    private static final String[] STAEDTE = {
        "Wien", "Graz", "Linz", "Salzburg", "Innsbruck", "Klagenfurt",
        "Villach", "Wels", "St. Pölten", "Dornbirn"
    };

    private static final String[] DIAGNOSEN = {
        "Akute Bronchitis", "Grippe", "Migräne", "Rückenschmerzen", "Hypertonie",
        "Diabetes mellitus Typ 2", "Arthrose", "Asthma bronchiale", "Gastritis",
        "Depression", "Angina pectoris", "Rheumatoide Arthritis", "Osteoporose"
    };

    private static final String[] MEDIKAMENTE = {
        "Aspirin", "Ibuprofen", "Paracetamol", "Amoxicillin", "Diclofenac",
        "Metformin", "Ramipril", "Simvastatin", "Omeprazol", "Pantoprazol"
    };

    private static final String[] WIRKSTOFFE = {
        "Acetylsalicylsäure", "Ibuprofen", "Paracetamol", "Amoxicillin", "Diclofenac",
        "Metformin", "Ramipril", "Simvastatin", "Omeprazol", "Pantoprazol"
    };

    /**
     * Generiert eine Liste von Ärzten
     */
    public static List<Arzt> generateAerzte(int count) {
        List<Arzt> aerzte = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            aerzte.add(generateArzt(i));
        }
        return aerzte;
    }

    /**
     * Generiert einen einzelnen Arzt
     */
    public static Arzt generateArzt(int index) {
        Fachgebiet[] fachgebiete = Fachgebiet.values();
        String vorname = VORNAMEN[random.nextInt(VORNAMEN.length)];
        String nachname = NACHNAMEN[random.nextInt(NACHNAMEN.length)];

        return Arzt.builder()
            .name("Dr. " + vorname + " " + nachname + "-" + index)
            .gebDatum(generateGeburtsdatum(1960, 1990))
            .svnr(generateSVNR())
            .fachgebiet(fachgebiete[random.nextInt(fachgebiete.length)])
            .email(Email.builder()
                .mail(vorname.toLowerCase() + "." + nachname.toLowerCase() + index + "@medical.at")
                .build())
            .adresse(generateAdresse())
            .telefonNummer(generateTelefon())
            .apiKey(UUID.randomUUID().toString())
            .build();
    }

    /**
     * Generiert eine Liste von Patienten
     */
    public static List<Patient> generatePatienten(int count) {
        List<Patient> patienten = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patienten.add(generatePatient(i));
        }
        return patienten;
    }

    /**
     * Generiert einen einzelnen Patienten
     */
    public static Patient generatePatient(int index) {
        Versicherungsart[] versicherungen = Versicherungsart.values();
        String vorname = VORNAMEN[random.nextInt(VORNAMEN.length)];
        String nachname = NACHNAMEN[random.nextInt(NACHNAMEN.length)];

        return Patient.builder()
            .name(vorname + " " + nachname + "-" + index)
            .gebDatum(generateGeburtsdatum(1940, 2010))
            .svnr(generateSVNR())
            .versicherungsart(versicherungen[random.nextInt(versicherungen.length)])
            .adresse(generateAdresse())
            .telefonNummer(generateTelefon())
            .apiKey(UUID.randomUUID().toString())
            .build();
    }

    /**
     * Generiert eine Liste von Behandlungen
     */
    public static List<Behandlung> generateBehandlungen(int count, List<Arzt> aerzte, List<Patient> patienten) {
        List<Behandlung> behandlungen = new ArrayList<>();

        if (aerzte.isEmpty() || patienten.isEmpty()) {
            throw new IllegalArgumentException("Ärzte und Patienten müssen vorhanden sein");
        }

        for (int i = 0; i < count; i++) {
            behandlungen.add(generateBehandlung(i, aerzte, patienten));
        }
        return behandlungen;
    }

    /**
     * Generiert eine einzelne Behandlung
     */
    public static Behandlung generateBehandlung(int index, List<Arzt> aerzte, List<Patient> patienten) {
        Arzt arzt = aerzte.get(random.nextInt(aerzte.size()));
        Patient patient = patienten.get(random.nextInt(patienten.size()));

        LocalDateTime beginn = generateBehandlungsDatum();
        int dauer = 15 + random.nextInt(120); // 15-135 Minuten

        return Behandlung.builder()
            .arzt(arzt)
            .patient(patient)
            .diagnose(DIAGNOSEN[random.nextInt(DIAGNOSEN.length)])
            .medikamente(generateMedikamente())
            .beginn(beginn)
            .ende(beginn.plusMinutes(dauer))
            .apiKey(UUID.randomUUID().toString())
            .build();
    }

    /**
     * Generiert eine Adresse
     */
    public static Adresse generateAdresse() {
        String strasse = STRASSEN[random.nextInt(STRASSEN.length)];
        String stadt = STAEDTE[random.nextInt(STAEDTE.length)];

        return Adresse.builder()
            .strasse(strasse)
            .hausNr(String.valueOf(1 + random.nextInt(200)))
            .stadt(stadt)
            .plz(generatePLZ(stadt))
            .build();
    }

    /**
     * Generiert eine Telefonnummer
     */
    public static TelefonNummer generateTelefon() {
        TelefonNummerArt[] arten = TelefonNummerArt.values();

        return new TelefonNummer(
            "043",
            String.format("%03d", random.nextInt(1000)),
            String.format("%08d", random.nextInt(100000000)),
            arten[random.nextInt(arten.length)]
        );
    }

    /**
     * Generiert eine Liste von Medikamenten
     */
    private static List<Medikament> generateMedikamente() {
        int anzahl = 1 + random.nextInt(3); // 1-3 Medikamente
        List<Medikament> medikamente = new ArrayList<>();

        for (int i = 0; i < anzahl; i++) {
            int index = random.nextInt(MEDIKAMENTE.length);
            medikamente.add(new Medikament(
                MEDIKAMENTE[index],
                WIRKSTOFFE[index]
            ));
        }

        return medikamente;
    }

    //Generiert ein zufälliges Geburtsdatum
    private static LocalDate generateGeburtsdatum(int startYear, int endYear) {
        int year = startYear + random.nextInt(endYear - startYear + 1);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28); // Vereinfacht, um ungültige Daten zu vermeiden
        return LocalDate.of(year, month, day);
    }

    //Generiert eine zufällige Sozialversicherungsnummer

    private static Long generateSVNR() {
        return 1000000000L + random.nextLong(9000000000L);
    }

    //Generiert ein Behandlungsdatum in den letzten 365 Tagen
    private static LocalDateTime generateBehandlungsDatum() {
        return LocalDateTime.now().minusDays(random.nextInt(365));
    }


     //Generiert eine PLZ basierend auf der Stadt

    private static String generatePLZ(String stadt) {
        return switch (stadt) {
            case "Wien" -> String.format("1%03d", random.nextInt(240));
            case "Graz" -> String.format("80%02d", random.nextInt(50));
            case "Linz" -> String.format("40%02d", random.nextInt(50));
            case "Salzburg" -> String.format("50%02d", random.nextInt(50));
            case "Innsbruck" -> String.format("60%02d", random.nextInt(50));
            case "Klagenfurt" -> String.format("90%02d", random.nextInt(50));
            case "Villach" -> String.format("94%02d", random.nextInt(50));
            case "Wels" -> String.format("46%02d", random.nextInt(50));
            case "St. Pölten" -> String.format("31%02d", random.nextInt(50));
            case "Dornbirn" -> String.format("69%02d", random.nextInt(50));
            default -> String.format("%04d", 1000 + random.nextInt(9000));
        };
    }

    //Gibt Statistiken über generierte Daten aus

    public static void printStatistics(int aerzte, int patienten, int behandlungen) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("TESTDATEN-STATISTIK");
        System.out.println("=".repeat(80));
        System.out.printf("Ärzte:        %,8d%n", aerzte);
        System.out.printf("Patienten:    %,8d%n", patienten);
        System.out.printf("Behandlungen: %,8d%n", behandlungen);
        System.out.printf("Gesamt:       %,8d Dokumente%n", aerzte + patienten + behandlungen);
        System.out.println("=".repeat(80) + "\n");
    }
}

