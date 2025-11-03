# MongoDB CRUD Performance Tests

## Überblick

Diese Performance-Tests messen die CRUD-Operationen (Create, Read, Update, Delete) auf MongoDB für die Entitäten:
- **Arzt**
- **Patient**
- **Behandlung**
- **Medikament**

Die Tests werden in verschiedenen Skalierungen durchgeführt: **100, 1.000, und 10.000** Datensätze.

## Voraussetzungen

1. Docker und Docker Compose installiert
2. Java 17 oder höher
3. Maven

## Tests starten

### 1. MongoDB starten

```bash
docker compose up mongo -d
```

### 2. Performance-Tests ausführen

```bash
./mvnw test -Dtest=MongoDBPerformanceTest
```

## Testübersicht

Die folgenden CRUD-Operationen werden getestet:

### Write-Operationen (2)
1. **Arzt-Inserts**: Bulk-Insert von Arzt-Entitäten
2. **Patient-Inserts**: Bulk-Insert von Patient-Entitäten

### Find-Operationen (4)
1. **Ohne Filter**: Alle Datensätze abrufen (`findAll()`)
2. **Mit Filter**: Suche nach Fachgebiet
3. **Mit Filter und Projektion**: Suche nach Fachgebiet mit selektierten Feldern
4. **Mit Filter, Projektion und Sortierung**: Suche nach Fachgebiet, selektierte Felder, sortiert nach Name

### Update-Operation (1)
- **Patient-Updates**: Massenaktualisierung von Versicherungsart

### Delete-Operation (1)
- **Behandlung-Deletes**: Massenl öschung von Behandlungen

## Testergebnisse

Beispielhafte Ergebnisse auf GitHub Actions Runner:

### Write Performance

#### Arzt-Inserts
- **100 Datensätze**: 97 ms (1.030 records/sec)
- **1.000 Datensätze**: 207 ms (4.831 records/sec)
- **10.000 Datensätze**: 708 ms (14.124 records/sec)

#### Patient-Inserts
- **100 Datensätze**: 33 ms (3.030 records/sec)
- **1.000 Datensätze**: 100 ms (10.000 records/sec)
- **10.000 Datensätze**: 713 ms (14.025 records/sec)

### Find Performance

#### Ohne Filter
- **100 Datensätze**: 64 ms (1.563 records/sec)
- **1.000 Datensätze**: 113 ms (8.850 records/sec)
- **10.000 Datensätze**: 329 ms (30.395 records/sec)

#### Mit Filter
- **100 Datensätze**: 9 ms (3.778 records/sec, 34 gefunden)
- **1.000 Datensätze**: 9 ms (37.111 records/sec, 334 gefunden)
- **10.000 Datensätze**: 60 ms (55.567 records/sec, 3.334 gefunden)

#### Mit Filter und Projektion
- **100 Datensätze**: 2 ms (17.000 records/sec, 34 gefunden)
- **1.000 Datensätze**: 5 ms (66.800 records/sec, 334 gefunden)
- **10.000 Datensätze**: 27 ms (123.481 records/sec, 3.334 gefunden)

#### Mit Filter, Projektion und Sortierung
- **100 Datensätze**: 2 ms (17.000 records/sec, 34 gefunden)
- **1.000 Datensätze**: 5 ms (66.800 records/sec, 334 gefunden)
- **10.000 Datensätze**: 26 ms (128.231 records/sec, 3.334 gefunden)

### Update Performance

#### Patient-Updates
- **100 Datensätze**: 108 ms (926 records/sec)
- **1.000 Datensätze**: 685 ms (1.460 records/sec)
- **10.000 Datensätze**: 5.118 ms (1.954 records/sec)

### Delete Performance

#### Behandlung-Deletes
- **100 Datensätze**: 2 ms (50.000 records/sec)
- **1.000 Datensätze**: 9 ms (111.111 records/sec)
- **10.000 Datensätze**: 89 ms (112.360 records/sec)

## Erkenntnisse

1. **Schreiboperationen**: MongoDB zeigt gute Leistung beim Bulk-Insert, mit durchschnittlich 10.000-14.000 records/sec bei großen Datenmengen.

2. **Leseoperationen**: 
   - Ohne Filter: Gute Performance, skaliert linear mit Datenmenge
   - Mit Filter: Sehr schnell durch Index auf Fachgebiet
   - Mit Projektion: Deutliche Verbesserung durch Reduzierung der übertragenen Daten
   - Mit Sortierung: Minimaler Overhead bei sortierten Abfragen

3. **Update-Operationen**: Moderatere Performance bei Massenaktualisierungen (~1.500-2.000 records/sec), da jedes Dokument einzeln aktualisiert wird.

4. **Delete-Operationen**: Sehr schnell (>50.000 records/sec) durch effiziente Collection-Drop Operation.

## Testklasse

Die Testklasse befindet sich unter:
```
src/test/java/com/example/zellervandecastellpatientenverwaltung/performance/MongoDBPerformanceTest.java
```

## Anmerkungen

- Die Tests verwenden Spring Boot Test mit echter MongoDB-Instanz (via Docker Compose)
- Jeder Test wird mit verschiedenen Skalierungen ausgeführt: 100, 1.000, 10.000 Datensätze
- Die Testdaten werden vor und nach jedem Test gelöscht
- Die Medikament-Entität wird als eingebettetes Dokument in Behandlung getestet
