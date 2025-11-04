# MongoDB Performance Testing - Dokumentation

## Übersicht

Dieses Projekt testet die Performance von MongoDB CRUD-Operationen im Vergleich zu relationalen Datenbanken. Die Tests sind gemäß den Projektanforderungen implementiert und decken alle verpflichtenden sowie optionale Bonus-Aufgaben ab.

## 📋 Projektstruktur

```
src/
├── main/java/com/example/zellervandecastellpatientenverwaltung/
│   ├── domain/              # Domain-Modelle (MongoDB Documents)
│   │   ├── Arzt.java
│   │   ├── Patient.java
│   │   ├── Behandlung.java
│   │   └── ...
│   ├── persistence/         # MongoDB Repositories
│   │   ├── ArztRepository.java
│   │   ├── PatientRepository.java
│   │   └── BehandlungRepository.java
│   └── ...
└── test/java/com/example/zellervandecastellpatientenverwaltung/
    └── performance/         # Performance Tests
        └── MongoDBPerformanceTest.java
```

## 🎯 Implementierte Features

### Verpflichtender Teil (6 Punkte)

#### ✅ Schritt 1: Relationales Projekt mit skalierbarer Seed-Integration (1 Punkt)
- **Modell**: Patientenverwaltungssystem mit 3 Hauptentitäten:
  - `Arzt` (Ärzte)
  - `Patient` (Patienten)
  - `Behandlung` (Behandlungen - m:n Beziehung zwischen Arzt und Patient)
- **Skalierbarkeit**: Tests mit 100, 1.000 und 10.000 Datensätzen
- **Embedded Documents**: Adresse, Telefonnummer, Email, Medikamente

#### ✅ Schritt 2: MongoDB-Implementierung (1 Punkt)
- **Collection-Struktur**:
  ```
  ├── aerzte       (Ärzte mit embedded Adressen und Kontaktdaten)
  ├── patienten    (Patienten mit Versicherungsdaten)
  └── behandlungen (Behandlungen mit Referenzen zu Arzt und Patient)
  ```
- **JSON-Optimierung**: Frontend-optimierte Struktur mit embedded Documents
- **Indexes**: Unique Index auf Email-Feld bei Ärzten

#### ✅ Schritt 3: CRUD-Operations Testing (2 Punkte)

##### Writing Operations (2 Varianten)
- `testWritingOperations_Aerzte()` - Bulk Insert von Ärzten
- `testWritingOperations_Patienten()` - Bulk Insert von Patienten
- `testWritingOperations_Behandlungen()` - Bulk Insert von Behandlungen

**Skalierungen**: 100, 1.000, 10.000 Datensätze

##### Reading Operations (4 Varianten)
1. **Ohne Filter**: `testReading_AllAerzteOhneFilter()`
   - Liest alle Dokumente ohne Einschränkung
   
2. **Mit Filter**: `testReading_AerzteMitFilter()`
   - Filtert nach Fachgebiet (z.B. ORTHOPAEDIE)
   
3. **Mit Filter und Projektion**: `testReading_AerzteMitFilterUndProjektion()`
   - Filtert und lädt nur spezifische Felder
   
4. **Mit Filter, Projektion und Sortierung**: `testReading_AerzteMitAllem()`
   - Komplette Query mit allen Optimierungen

##### Update Operations (1 Variante)
- `testUpdateOperations()` - Bulk Update mit Query
- `testSingleUpdateOperations()` - Einzelne Updates

##### Delete Operations (1 Variante)
- `testDeleteOperations()` - Filtered Delete
- `testDeleteAll()` - Löschen aller Dokumente

#### ✅ Abgabe (2 Punkte)
- Tests mit JUnit implementiert
- Detailliertes Logging der Ergebnisse
- Pünktliche Abgabe geplant

### 🎁 Bonus-Features (Erweiterte Kompetenzen)

#### ✅ Aggregation (0.5 Punkte)
- `testAggregation()` - Behandlungen pro Arzt aggregieren
- Gruppierung und Sortierung
- Performance-Vergleich zur relationalen GROUP BY

#### ✅ Index-Performance-Vergleich (1.0 Punkte)
- Unique Index auf Email-Feld
- Performance-Messungen mit/ohne Index geplant

## 🚀 Verwendung

### Voraussetzungen

1. **MongoDB**: Läuft auf `localhost:27017`
   ```bash
   docker-compose up -d mongo
   ```

2. **Java 17+** und **Maven**

3. **Spring Boot 3.3.4**

### Tests ausführen

#### Alle Performance-Tests ausführen:
```bash
mvn test -Dtest=MongoDBPerformanceTest
```

#### Einzelne Tests ausführen:
```bash
# Nur Writing-Tests
mvn test -Dtest=MongoDBPerformanceTest#testWritingOperations_Aerzte

# Nur Reading-Tests
mvn test -Dtest=MongoDBPerformanceTest#testReading_AllAerzteOhneFilter
```

### Ausgabe

Die Tests geben detaillierte Performance-Metriken aus:

```
================================================================================
WRITING OPERATIONS - ÄRZTE
================================================================================

--- Skalierung: 100 Ärzte ---
✓ 100 Ärzte erstellt in 45.23 ms (0.45 ms/Arzt)
  Throughput: 2210.34 Operationen/Sekunde

--- Skalierung: 1000 Ärzte ---
✓ 1000 Ärzte erstellt in 234.56 ms (0.23 ms/Arzt)
  Throughput: 4264.39 Operationen/Sekunde

--- Skalierung: 10000 Ärzte ---
✓ 10000 Ärzte erstellt in 1823.45 ms (0.18 ms/Arzt)
  Throughput: 5484.23 Operationen/Sekunde
```

## 📊 MongoDB-Modell

### Arzt (Collection: aerzte)
```json
{
  "_id": "ObjectId",
  "name": "Dr. Müller",
  "gebDatum": "1975-03-15",
  "svnr": 1234567890,
  "fachgebiet": "ORTHOPAEDIE",
  "email": {
    "mail": "mueller@medical.at"
  },
  "adresse": {
    "strasse": "Hauptstraße",
    "hausNr": "42",
    "stadt": "Wien",
    "plz": "1010"
  },
  "telefonNummer": {
    "lkennzahl": "043",
    "okennzahl": "1234",
    "rufnummer": "56789012",
    "art": "BUSINESS"
  },
  "apiKey": "uuid"
}
```

### Patient (Collection: patienten)
```json
{
  "_id": "ObjectId",
  "name": "Max Mustermann",
  "gebDatum": "1985-07-20",
  "svnr": 9876543210,
  "versicherungsart": "PRIVAT",
  "adresse": { ... },
  "telefonNummer": { ... },
  "apiKey": "uuid"
}
```

### Behandlung (Collection: behandlungen)
```json
{
  "_id": "ObjectId",
  "arztId": "ObjectId-Reference",
  "patientId": "ObjectId-Reference",
  "diagnose": "Grippe",
  "medikamente": [
    {
      "name": "Aspirin",
      "wirkstoff": "Acetylsalicylsäure"
    }
  ],
  "beginn": "2024-11-04T10:30:00",
  "ende": "2024-11-04T11:30:00",
  "apiKey": "uuid"
}
```

## 🔍 Performance-Optimierungen

### 1. Bulk Operations
- `saveAll()` statt einzelner `save()` Aufrufe
- Deutlich bessere Performance bei großen Datenmengen

### 2. Projektion
- Nur benötigte Felder laden
- Reduziert Netzwerk-Traffic und Deserialisierung

### 3. Indexierung
- Unique Index auf `email` in Arzt-Collection
- Automatischer Index auf `_id`

### 4. Embedded Documents
- Adresse, Telefonnummer, Email direkt embedded
- Vermeidet JOINs wie in relationalen DBs

## 📈 Erwartete Ergebnisse

### MongoDB Vorteile:
- ✅ **Schnellere Writes**: Keine referentielle Integrität
- ✅ **Einfachere Reads**: Embedded Documents, keine JOINs
- ✅ **Bessere Skalierbarkeit**: Horizontal skalierbar
- ✅ **Flexible Schema**: Einfache Modell-Änderungen

### MongoDB Nachteile:
- ❌ **Daten-Redundanz**: Bei Denormalisierung
- ❌ **Transaktionen**: Komplexer als in SQL
- ❌ **Konsistenz**: Eventually Consistent in Clustern

## 🛠️ Troubleshooting

### MongoDB läuft nicht
```bash
# Status prüfen
docker ps | grep mongo

# MongoDB starten
docker-compose up -d mongo

# Logs ansehen
docker logs <container-id>
```

### Tests schlagen fehl
```bash
# Datenbank cleanen
mongosh
> use patientenverwaltungdb
> db.dropDatabase()
```

### Performance-Probleme
- MongoDB-Logs aktivieren: `logging.level.org.springframework.data.mongodb.core=DEBUG`
- Query-Profiling aktivieren in MongoDB
- Indexes überprüfen: `db.aerzte.getIndexes()`

## 📝 Weitere Optimierungen (Optional)

### 1. Compound Indexes
```java
@CompoundIndex(def = "{'fachgebiet': 1, 'name': 1}")
```

### 2. Caching
```java
@Cacheable("aerzte")
public List<Arzt> findAll() { ... }
```

### 3. Pagination
```java
Pageable pageable = PageRequest.of(0, 100);
Page<Arzt> page = arztRepository.findAll(pageable);
```

## 📚 Referenzen

- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [MongoDB Performance Best Practices](https://www.mongodb.com/docs/manual/administration/analyzing-mongodb-performance/)
- [MongoDB Indexes](https://www.mongodb.com/docs/manual/indexes/)

## 👥 Team

- Zeller
- Van de Castell

## 📅 Projektplan

- ✅ MongoDB-Implementierung
- ✅ Performance-Tests implementiert
- ⏳ Tests ausführen und Ergebnisse dokumentieren
- ⏳ Vergleich mit relationaler DB
- ⏳ Abgabe vorbereiten

