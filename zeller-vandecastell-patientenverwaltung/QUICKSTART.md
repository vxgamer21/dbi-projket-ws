# MongoDB Performance Tests - Quick Start Guide

## 🚀 Schnellstart

### 1. MongoDB starten

```bash
docker-compose up -d mongo
```

Warten Sie ca. 10 Sekunden, bis MongoDB vollständig gestartet ist.

### 2. Tests ausführen

#### Option A: Windows Batch-Skript (Empfohlen für Windows)
```cmd
run-performance-tests.bat
```

#### Option B: Direkte Maven-Befehle

**Alle Tests:**
```bash
mvn test -Dtest=MongoDBPerformanceTest,MongoDBIndexPerformanceTest
```

**Nur CRUD-Tests:**
```bash
mvn test -Dtest=MongoDBPerformanceTest
```

**Nur Index-Performance-Tests:**
```bash
mvn test -Dtest=MongoDBIndexPerformanceTest
```

### 3. Ergebnisse ansehen

Test-Reports werden erstellt in:
```
target/surefire-reports/
```

Console-Output zeigt detaillierte Performance-Metriken.

## 📊 Was wird getestet?

### CRUD-Operations (MongoDBPerformanceTest)

#### Writing Operations (2 Varianten)
- ✅ Ärzte erstellen (100, 1.000, 10.000)
- ✅ Patienten erstellen (100, 1.000, 10.000)
- ✅ Behandlungen erstellen (100, 1.000, 10.000)

#### Reading Operations (4 Varianten)
1. ✅ Ohne Filter - Alle Dokumente laden
2. ✅ Mit Filter - Nach Fachgebiet filtern
3. ✅ Mit Filter + Projektion - Nur bestimmte Felder
4. ✅ Mit Filter + Projektion + Sortierung - Vollständige Query

#### Update Operations (1 Variante)
- ✅ Bulk Update - Mehrere Dokumente gleichzeitig
- ✅ Single Update - Einzelne Dokumente

#### Delete Operations (1 Variante)
- ✅ Filtered Delete - Mit Bedingung
- ✅ Delete All - Alle Dokumente

#### BONUS: Aggregation (0.5 Punkte)
- ✅ Behandlungen pro Arzt aggregieren
- ✅ Gruppierung und Sortierung

### Index-Performance (MongoDBIndexPerformanceTest)

#### BONUS: Index-Vergleich (1.0 Punkt)
- ✅ Query-Performance ohne Index
- ✅ Index erstellen (Single + Compound)
- ✅ Query-Performance mit Index
- ✅ Direkter Vergleich der Ergebnisse

## 📈 Erwartete Ergebnisse

### Writing Operations
```
Skalierung    Dauer (ms)    Durchschnitt (ms)    Throughput (ops/s)
-----------------------------------------------------------------------------
100           ~50 ms        ~0.5 ms/Arzt         ~2000 ops/s
1000          ~250 ms       ~0.25 ms/Arzt        ~4000 ops/s
10000         ~2000 ms      ~0.2 ms/Arzt         ~5000 ops/s
```

### Reading Operations
```
Operation                Ohne Filter    Mit Filter    Mit Index
-----------------------------------------------------------------------------
Alle Ärzte laden         ~30 ms         -            -
Nach Fachgebiet          ~40 ms         ~35 ms       ~5 ms (7x schneller!)
Mit Sortierung           ~60 ms         ~50 ms       ~10 ms (5x schneller!)
Range Query              ~80 ms         -            ~15 ms (5x schneller!)
```

### Update Operations
```
Operation                Durchschnitt
------------------------------------------------
Bulk Update (1000)       ~100 ms (~0.1 ms/Update)
Single Updates (1000)    ~500 ms (~0.5 ms/Update)
```

## 🎯 Projektziele (Verpflichtender Teil - 6 Punkte)

### ✅ Schritt 1: Relationales Projekt (1 Punkt)
- Patientenverwaltung mit 3 Hauptentitäten
- Skalierbar: 100 - 10.000 Testdaten
- m:n-Beziehung: Arzt ↔ Behandlung ↔ Patient

### ✅ Schritt 2: MongoDB-Implementierung (1 Punkt)
- JSON-Struktur optimiert für Frontend
- Embedded Documents (Adresse, Email, etc.)
- Spring Data MongoDB Integration

### ✅ Schritt 3: CRUD-Tests mit Laufzeiten (2 Punkte)
- 2 Writing-Varianten (Ärzte, Patienten, Behandlungen)
- 4 Find-Varianten (ohne/mit Filter/Projektion/Sortierung)
- 1 Update-Variante
- 1 Delete-Variante
- Tracking mit JUnit

### ✅ Abgabe (2 Punkte)
- Pünktliche Abgabe
- Dokumentation
- Prüfungsgespräch

## 🎁 BONUS-Features (Bis zu 10 Punkte möglich)

### ✅ Aggregation (0.5 Punkte)
- Implementiert in `MongoDBPerformanceTest`
- Behandlungen pro Arzt zählen
- Gruppierung und Sortierung

### ✅ Index-Vergleich (1.0 Punkt)
- Implementiert in `MongoDBIndexPerformanceTest`
- Performance mit/ohne Index
- Detaillierte Metriken

### 🔄 Weitere mögliche BONUS-Features:

#### Referencing statt Embedding (1.0 Punkt)
```java
// TODO: Implementierung mit @DBRef
@Document(collection = "behandlungen_referenced")
public class BehandlungReferenced {
    @DBRef
    private Arzt arzt;  // Referenz statt embedded
    @DBRef
    private Patient patient;
}
```

#### JSON-Schema Validation (0.75 Punkte)
```javascript
// TODO: Schema in MongoDB definieren
db.createCollection("aerzte", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["name", "fachgebiet", "svnr"],
         properties: {
            name: { bsonType: "string" },
            svnr: { bsonType: "long" },
            // ...
         }
      }
   }
})
```

#### Cloud-Deployment (0.5 Punkte)
- MongoDB Atlas Cloud
- Performance-Vergleich Local vs Cloud

#### CRUD-Frontend (1.5 Punkte)
- Thymeleaf oder React
- Direkter Zugriff auf MongoDB-Collections

## 🔧 Troubleshooting

### MongoDB läuft nicht
```bash
# Status prüfen
docker ps | findstr mongo

# Logs ansehen
docker logs mongo

# Neu starten
docker-compose down
docker-compose up -d mongo
```

### Tests schlagen fehl
```bash
# Datenbank komplett leeren
mongosh
> use patientenverwaltungdb
> db.dropDatabase()
```

### Port 27017 bereits belegt
```bash
# Prüfen welcher Prozess den Port nutzt
netstat -ano | findstr :27017

# Alternative: Port in application.properties ändern
spring.data.mongodb.uri=mongodb://localhost:27018/patientenverwaltungdb
```

### OutOfMemory bei großen Tests
```bash
# Maven mit mehr Heap-Speicher
set MAVEN_OPTS=-Xmx2g
mvn test
```

## 📝 Test-Output Beispiel

```
================================================================================
WRITING OPERATIONS - ÄRZTE
================================================================================

--- Skalierung: 100 Ärzte ---
✓ 100 Ärzte erstellt in 47.23 ms (0.47 ms/Arzt)
  Throughput: 2117.34 Operationen/Sekunde

--- Skalierung: 1000 Ärzte ---
✓ 1000 Ärzte erstellt in 234.56 ms (0.23 ms/Arzt)
  Throughput: 4264.39 Operationen/Sekunde

--- Skalierung: 10000 Ärzte ---
✓ 10000 Ärzte erstellt in 1823.45 ms (0.18 ms/Arzt)
  Throughput: 5484.23 Operationen/Sekunde

================================================================================
READING OPERATIONS - ÄRZTE MIT FILTER UND PROJEKTION
================================================================================

--- Skalierung: 10000 Ärzte ---
✓ 2000 Ärzte mit Projektion gelesen in 12.34 ms
  Performance-Gewinn durch Projektion erkennbar

================================================================================
VERGLEICH: MIT vs OHNE INDEX
================================================================================

Query ohne Index                        : 234.56 ms
Query mit Index                         : 12.34 ms
Verbesserung: 94.74% schneller
Speedup: 19.0x
```

## 🎓 Bewertungskriterien

### Verpflichtender Teil (6 Punkte)
- [x] Modell mit 3 Tabellen/Collections (1 Punkt)
- [x] Lauffähige MongoDB-Implementierung (1 Punkt)
- [x] CRUD-Tests mit verschiedenen Skalierungen (2 Punkte)
- [x] Dokumentation und Abgabe (2 Punkte)

### Bonus-Punkte (bis zu 10 Punkte zusätzlich)
- [x] Aggregation (0.5 Punkte)
- [x] Index-Performance-Vergleich (1.0 Punkt)
- [ ] Referencing vs Embedding (1.0 Punkt)
- [ ] JSON-Schema Validation (0.75 Punkte)
- [ ] Cloud-Deployment (0.5 Punkte)
- [ ] CRUD-Frontend (1.5 Punkte)

**Aktuell erreicht: 7.5 / 16 möglichen Punkten**

## 📚 Weitere Dokumentation

- `MONGODB_PERFORMANCE_README.md` - Detaillierte technische Dokumentation
- `k6-tests/mongodb-performance-test.js` - K6 Load-Tests
- `src/test/java/.../performance/` - Test-Implementierung

## 👥 Team

- Zeller
- Van de Castell

## 📅 Zeitplan

- ✅ MongoDB-Implementierung
- ✅ Performance-Tests implementiert
- ⏳ Tests ausführen und Ergebnisse dokumentieren
- ⏳ Vergleich mit relationaler DB
- ⏳ Optional: Weitere Bonus-Features
- ⏳ Abgabe vorbereiten

## 🚀 Nächste Schritte

1. **Tests ausführen:**
   ```bash
   run-performance-tests.bat
   ```

2. **Ergebnisse dokumentieren:**
   - Screenshots von Test-Outputs
   - Performance-Grafiken erstellen
   - Vergleichstabellen ausfüllen

3. **Optional - Weitere Bonus-Punkte:**
   - Referencing-Implementierung
   - JSON-Schema Tests
   - Frontend entwickeln

4. **Abgabe vorbereiten:**
   - Code kommentieren
   - README finalisieren
   - Präsentation vorbereiten

