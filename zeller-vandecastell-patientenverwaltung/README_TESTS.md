# MongoDB Performance Testing - Quick Start

## Zusammenfassung

Dieses Projekt enthält umfassende Performance-Tests für MongoDB CRUD-Operationen auf den Entitäten:
- **Arzt** (Ärzte)
- **Patient** (Patienten)  
- **Behandlung** (Behandlungen mit eingebetteten Medikamenten)
- **Medikament** (als eingebettetes Dokument)

## Schnellstart

### 1. MongoDB starten

```bash
cd zeller-vandecastell-patientenverwaltung
docker compose up mongo -d
```

### 2. Tests ausführen

```bash
./mvnw test -Dtest=MongoDBPerformanceTest
```

### 3. MongoDB stoppen

```bash
docker compose down
```

## Was wird getestet?

✅ **2 Write-Operationen** (Arzt-Inserts, Patient-Inserts)  
✅ **4 Find-Operationen** (ohne Filter, mit Filter, mit Filter+Projektion, mit Filter+Projektion+Sortierung)  
✅ **1 Update-Operation** (Patient-Bulk-Updates)  
✅ **1 Delete-Operation** (Behandlung-Löschungen)

Jeder Test wird in 3 Skalierungen durchgeführt: **100, 1.000, 10.000 Datensätze**

## Ergebnisse

Alle Tests zeigen die Laufzeiten und Durchsatzraten (records/sec) für jede Operation.

Detaillierte Ergebnisse und Analyse siehe: [PERFORMANCE_TESTS.md](./PERFORMANCE_TESTS.md)

## Hinweis

- Die Tests verwenden Spring Boot mit echter MongoDB-Instanz (nicht in-memory)
- Testdaten werden automatisch vor und nach jedem Test gelöscht
- Die Test-Infrastruktur ist für Arzt, Patient, Behandlung und Medikament optimiert
- Alle nicht benötigten Entitäten (Arztpraxis, Mitarbeiter, Rechnung, etc.) wurden entfernt
