# SQL Performance Tests - Dokumentation

## Übersicht

Die SQL Performance Tests sind entwickelt worden, um die Leistung der relationalen Datenbank (PostgreSQL) für verschiedene CRUD-Operationen zu messen und zu analysieren.

## Test-Klassen

### 1. SQLPerformanceTest.java
Grundlegende Performance-Tests für alle geforderten Operationen.

### 2. SQLDetailedPerformanceTest.java
Erweiterte Performance-Tests mit detaillierter Auswertung und Statistiken.

## Getestete Operationen

### WRITE Operations (2 verschiedene)
1. **Patienten erstellen** - Test in verschiedenen Skalierungen (100, 1.000, 10.000)
2. **Behandlungen erstellen** - Test in verschiedenen Skalierungen (100, 1.000, 10.000)

### FIND Operations (4 verschiedene)
1. **Ohne Filter** - `SELECT * FROM patient` - Alle Datensätze abrufen
2. **Mit Filter** - `SELECT * FROM patient WHERE name LIKE '%Pattern%'` - Filterung mit WHERE-Klausel
3. **Mit Filter und Projektion** - `SELECT id, name, svnr FROM patient WHERE ...` - Nur bestimmte Spalten abrufen
4. **Mit Filter, Projektion und Sortierung** - `SELECT ... FROM behandlung JOIN patient JOIN arzt ORDER BY ...` - Komplexe Abfrage mit JOINs

### UPDATE Operation (1)
- **Massen-Update** - 1.000 Patienten gleichzeitig aktualisieren

### DELETE Operation (1)
- **Massen-Delete** - 1.000 Patienten gleichzeitig löschen

## Ausführung der Tests

### Einzelner Test
```bash
mvnw test -Dtest=SQLPerformanceTest
```

### Detaillierter Test mit Auswertung
```bash
mvnw test -Dtest=SQLDetailedPerformanceTest
```

### Alle Performance Tests
```bash
mvnw test -Dtest=SQL*PerformanceTest
```

## Voraussetzungen

1. **PostgreSQL Datenbank** muss laufen
   - Host: localhost
   - Port: 5432
   - Database: patientenverwaltung
   - User: patientenverwaltungowner
   - Password: patientenverwaltungowner

2. **Spring Boot Anwendung** muss konfiguriert sein
   - application.properties mit korrekten Datenbankeinstellungen

## Erwartete Ausgabe

### SQLDetailedPerformanceTest Beispielausgabe:

```
╔════════════════════════════════════════════════════════════════╗
║     SQL PERFORMANCE TEST - RELATIONALE DATENBANK             ║
╚════════════════════════════════════════════════════════════════╝

[WRITE 1] Patienten erstellen in verschiedenen Skalierungen
─────────────────────────────────────────────────────────────
  ✓    100 Patienten:    234 ms (  0.234 s) - Durchsatz:    427 Datensätze/s
  ✓   1000 Patienten:   1845 ms (  1.845 s) - Durchsatz:    542 Datensätze/s
  ✓  10000 Patienten:  18923 ms ( 18.923 s) - Durchsatz:    528 Datensätze/s

[WRITE 2] Behandlungen erstellen in verschiedenen Skalierungen
─────────────────────────────────────────────────────────────
  ✓    100 Behandlungen:    312 ms (  0.312 s) - Durchsatz:    320 Datensätze/s
  ✓   1000 Behandlungen:   2567 ms (  2.567 s) - Durchsatz:    389 Datensätze/s
  ✓  10000 Behandlungen:  25123 ms ( 25.123 s) - Durchsatz:    398 Datensätze/s

[FIND 1] Alle Datensätze ohne Filter abrufen
─────────────────────────────────────────────────────────────
  ✓ Gefunden: 1000 Patienten in 45 ms

[FIND 2] Datensätze mit Filter abrufen
─────────────────────────────────────────────────────────────
  ✓ Gefunden: 111 Patienten (Filter: LIKE '%Patient 1%') in 23 ms

[FIND 3] Datensätze mit Filter und Projektion abrufen
─────────────────────────────────────────────────────────────
  ✓ Gefunden: 1000 Patienten (nur ausgewählte Felder) in 38 ms

[FIND 4] Datensätze mit Filter, Projektion und Sortierung abrufen
─────────────────────────────────────────────────────────────
  ✓ Gefunden: 1000 Behandlungen (mit JOIN, Projektion) in 67 ms

[UPDATE] Massendaten aktualisieren
─────────────────────────────────────────────────────────────
  ✓ Updated: 1000 Patienten in 1234 ms - Durchsatz: 810 Updates/s

[DELETE] Massendaten löschen
─────────────────────────────────────────────────────────────
  ✓ Gelöscht: 1000 Patienten in 89 ms - Durchsatz: 11235 Deletes/s

╔═══════════════════════════════════════════════════════════════════════════════╗
║                        PERFORMANCE TEST ZUSAMMENFASSUNG                       ║
╚═══════════════════════════════════════════════════════════════════════════════╝

┌───────────────────────────────────────────────────────────────────────────────┐
│ WRITE OPERATIONS                                                              │
└───────────────────────────────────────────────────────────────────────────────┘
  [WRITE-1] Patienten erstellen              │ Datensätze:    100 │ Zeit:    234 ms
  [WRITE-1] Patienten erstellen              │ Datensätze:   1000 │ Zeit:   1845 ms
  [WRITE-1] Patienten erstellen              │ Datensätze:  10000 │ Zeit:  18923 ms
  [WRITE-2] Behandlungen erstellen           │ Datensätze:    100 │ Zeit:    312 ms
  [WRITE-2] Behandlungen erstellen           │ Datensätze:   1000 │ Zeit:   2567 ms
  [WRITE-2] Behandlungen erstellen           │ Datensätze:  10000 │ Zeit:  25123 ms

┌───────────────────────────────────────────────────────────────────────────────┐
│ FIND OPERATIONS                                                               │
└───────────────────────────────────────────────────────────────────────────────┘
  [FIND-1] Alle ohne Filter                  │ Datensätze:   1000 │ Zeit:     45 ms
  [FIND-2] Mit Filter (LIKE)                 │ Datensätze:    111 │ Zeit:     23 ms
  [FIND-3] Mit Projektion                    │ Datensätze:   1000 │ Zeit:     38 ms
  [FIND-4] Mit Projektion + Sortierung       │ Datensätze:   1000 │ Zeit:     67 ms

┌───────────────────────────────────────────────────────────────────────────────┐
│ UPDATE OPERATION                                                              │
└───────────────────────────────────────────────────────────────────────────────┘
  [UPDATE] Patienten aktualisieren           │ Datensätze:   1000 │ Zeit:   1234 ms

┌───────────────────────────────────────────────────────────────────────────────┐
│ DELETE OPERATION                                                              │
└───────────────────────────────────────────────────────────────────────────────┘
  [DELETE] Patienten löschen                 │ Datensätze:   1000 │ Zeit:     89 ms

┌───────────────────────────────────────────────────────────────────────────────┐
│ GESAMTSTATISTIK                                                               │
└───────────────────────────────────────────────────────────────────────────────┘
  • Anzahl Tests: 11
  • Gesamtzeit: 50500 ms (50.50 s)
  • Durchschnittliche Zeit pro Test: 4591 ms

╔═══════════════════════════════════════════════════════════════════════════════╗
║                              TEST ABGESCHLOSSEN                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

## Gemessene Metriken

- **Ausführungszeit** in Millisekunden (ms)
- **Durchsatz** (Datensätze pro Sekunde)
- **Anzahl verarbeiteter Datensätze**
- **Skalierungsverhalten** (100, 1.000, 10.000 Datensätze)

## Testdaten-Struktur

### Patienten
- Name, Geburtsdatum, SVN, Adresse, Telefonnummer, Versicherungsart

### Ärzte
- Name, Geburtsdatum, SVN, Adresse, Telefonnummer, Fachgebiet, Email

### Behandlungen
- Arzt (Foreign Key), Patient (Foreign Key), Beginn, Ende, Diagnose, Medikamente (ElementCollection)

## Hinweise

1. Die Tests verwenden `@DirtiesContext` um sicherzustellen, dass die Datenbank nach jedem Test-Lauf sauber ist.
2. Tests sind mit `@Order` annotiert, um eine deterministische Ausführungsreihenfolge zu garantieren.
3. Die Tests messen die Zeit mit `System.nanoTime()` für höhere Präzision.
4. Batch-Operationen werden mit `saveAll()` und `deleteAll()` durchgeführt für bessere Performance.

## Erweiterungsmöglichkeiten

- Hinzufügen von Indizes und Messung des Performance-Unterschieds
- Tests mit verschiedenen Transaction-Isolation-Levels
- Parallel vs. Sequential Processing Tests
- Connection Pool Konfiguration Tests
- Query Plan Analyse

## Vergleich mit MongoDB

Diese SQL Tests bilden die Basis für den Vergleich mit MongoDB-Tests. Die gleiche Struktur sollte für MongoDB-Tests verwendet werden, um einen fairen Vergleich zu ermöglichen.

