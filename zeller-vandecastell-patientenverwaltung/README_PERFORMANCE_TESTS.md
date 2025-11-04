# MongoDB Performance Testing - Patientenverwaltung

> Vergleich von MongoDB mit relationalen Datenbanken anhand eines Patientenverwaltungssystems

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Java Version](https://img.shields.io/badge/Java-17-orange.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)]()
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)]()

---

## 📋 Projektübersicht

Dieses Projekt implementiert eine **Patientenverwaltung** mit MongoDB und führt umfassende Performance-Tests durch, um die Unterschiede zwischen NoSQL (MongoDB) und relationalen Datenbanken zu analysieren.

### Hauptziele
- ✅ MongoDB-Implementierung eines Patientenverwaltungssystems
- ✅ Performance-Tests für CRUD-Operationen
- ✅ Vergleich verschiedener Skalierungen (100 - 10.000 Datensätze)
- ✅ Index-Performance-Analyse
- ✅ Aggregations-Performance

---

## 🚀 Quick Start

### 1. MongoDB starten
```bash
docker-compose up -d mongo
```

### 2. Tests ausführen
```bash
# Windows
.\mvnw.cmd test -Dtest=MongoDBPerformanceTest

# Unix/Linux/Mac
./mvnw test -Dtest=MongoDBPerformanceTest
```

### 3. Ergebnisse ansehen
```bash
# Test-Reports
target/surefire-reports/

# MongoDB UI (optional)
http://localhost:8081
```

**→ Siehe [QUICKSTART.md](QUICKSTART.md) für detaillierte Anleitung**

---

## 📊 Datenmodell

### Collections

```
MongoDB Database: patientenverwaltungdb
│
├── aerzte          (Ärzte mit embedded Kontaktdaten)
├── patienten       (Patienten mit Versicherungsinformationen)
└── behandlungen    (Behandlungen mit Referenzen)
```

### Arzt (aerzte)
```json
{
  "_id": "ObjectId",
  "name": "Dr. Max Mustermann",
  "gebDatum": "1975-05-15",
  "svnr": 1234567890,
  "fachgebiet": "ORTHOPAEDIE",
  "email": { "mail": "max@medical.at" },
  "adresse": { /* embedded */ },
  "telefonNummer": { /* embedded */ }
}
```

### Patient (patienten)
```json
{
  "_id": "ObjectId",
  "name": "Anna Meier",
  "gebDatum": "1985-03-20",
  "svnr": 9876543210,
  "versicherungsart": "PRIVAT",
  "adresse": { /* embedded */ },
  "telefonNummer": { /* embedded */ }
}
```

### Behandlung (behandlungen)
```json
{
  "_id": "ObjectId",
  "arztId": "ObjectId-Reference",
  "patientId": "ObjectId-Reference",
  "diagnose": "Grippe",
  "medikamente": [
    { "name": "Aspirin", "wirkstoff": "..." }
  ],
  "beginn": "2024-11-04T10:30:00",
  "ende": "2024-11-04T11:30:00"
}
```

---

## 🧪 Tests

### CRUD-Operations (MongoDBPerformanceTest)

#### ✅ Writing Operations
- Ärzte erstellen (100, 1K, 10K)
- Patienten erstellen (100, 1K, 10K)
- Behandlungen erstellen (100, 1K, 10K)

#### ✅ Reading Operations
- Ohne Filter
- Mit Filter (Fachgebiet, Versicherungsart)
- Mit Filter + Projektion
- Mit Filter + Projektion + Sortierung

#### ✅ Update Operations
- Bulk Updates
- Einzelne Updates

#### ✅ Delete Operations
- Filtered Delete
- Delete All

#### ✅ BONUS: Aggregation
- Behandlungen pro Arzt
- Gruppierung & Sortierung

### Index-Performance (MongoDBIndexPerformanceTest)

#### ✅ Vergleich mit/ohne Index
- Performance-Messung ohne Index
- Index-Erstellung (Single + Compound)
- Performance-Messung mit Index
- Speedup-Berechnung

---

## 📈 Beispiel-Ergebnisse

### Writing Operations
| Skalierung | Dauer | Durchschnitt | Throughput |
|-----------|-------|--------------|------------|
| 100       | ~50ms | ~0.5ms/Doc   | ~2000 ops/s |
| 1.000     | ~250ms | ~0.25ms/Doc | ~4000 ops/s |
| 10.000    | ~2s   | ~0.2ms/Doc   | ~5000 ops/s |

### Index-Performance
| Operation | Ohne Index | Mit Index | Speedup |
|-----------|-----------|-----------|---------|
| Einfache Query | 234ms | 12ms | **19.5x** |
| Mit Sortierung | 456ms | 23ms | **19.8x** |
| Range Query | 678ms | 34ms | **19.9x** |

---

## 🏗️ Projektstruktur

```
zeller-vandecastell-patientenverwaltung/
│
├── src/
│   ├── main/java/.../
│   │   ├── domain/              # MongoDB Documents
│   │   ├── persistence/         # Repositories
│   │   ├── service/             # Business Logic
│   │   └── presentation/        # Controllers
│   │
│   └── test/java/.../performance/
│       ├── MongoDBPerformanceTest.java      ⭐ CRUD-Tests
│       ├── MongoDBIndexPerformanceTest.java ⭐ Index-Tests
│       ├── TestDataGenerator.java
│       └── PerformanceMetrics.java
│
├── k6-tests/
│   └── mongodb-performance-test.js          ⭐ Load-Tests
│
├── compose.yml                              # Docker Setup
├── pom.xml                                  # Dependencies
│
├── QUICKSTART.md                            ⭐ Schnellstart
├── MONGODB_PERFORMANCE_README.md            ⭐ Tech-Docs
├── IMPLEMENTATION_COMPLETE.md               ⭐ Status
├── ABGABE_CHECKLISTE.md                     ⭐ Checkliste
│
├── run-performance-tests.bat                # Windows
└── run-performance-tests.sh                 # Unix
```

---

## 🛠️ Technologie-Stack

### Backend
- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data MongoDB**
- **Lombok**

### Database
- **MongoDB 7.0**
- **Mongo Express** (Admin UI)

### Testing
- **JUnit 5**
- **Testcontainers**
- **K6** (Load Testing)

### Build & Deployment
- **Maven**
- **Docker & Docker Compose**

---

## 📚 Dokumentation

| Dokument | Beschreibung |
|----------|-------------|
| [QUICKSTART.md](QUICKSTART.md) | Schnellstart-Anleitung |
| [MONGODB_PERFORMANCE_README.md](MONGODB_PERFORMANCE_README.md) | Technische Details |
| [IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md) | Implementierungs-Status |
| [ABGABE_CHECKLISTE.md](ABGABE_CHECKLISTE.md) | Abgabe-Checkliste |

---

## 🎯 Projektziele & Bewertung

### Verpflichtender Teil (6 Punkte)
- ✅ Modell mit 3 Collections (1 Punkt)
- ✅ Lauffähige Implementierung (1 Punkt)
- ✅ CRUD-Tests mit Skalierung (2 Punkte)
- ⏳ Pünktliche Abgabe (1 Punkt)
- ⏳ Prüfungsgespräch (1 Punkt)

### Bonus-Features (bis zu 10 Punkte)
- ✅ Aggregation (0.5 Punkte)
- ✅ Index-Performance (1.0 Punkt)
- ⏳ Referencing vs Embedding (1.0 Punkt)
- ⏳ JSON-Schema Validation (0.75 Punkte)
- ⏳ Cloud-Deployment (0.5 Punkte)
- ⏳ CRUD-Frontend (1.5 Punkte)

**Aktuell: 5.5 / 11.25 Punkte**  
**Bei Abgabe: 7.5 / 11.25 Punkte**

---

## 🔧 Installation & Setup

### Voraussetzungen
- Java 17+
- Docker & Docker Compose
- Maven (oder verwende mvnw)

### Installation
```bash
# 1. Repository klonen (falls vorhanden)
git clone <repository-url>
cd zeller-vandecastell-patientenverwaltung

# 2. MongoDB starten
docker-compose up -d mongo

# 3. Projekt kompilieren
./mvnw clean compile

# 4. Tests ausführen
./mvnw test -Dtest=MongoDBPerformanceTest
```

---

## 🧪 Tests ausführen

### Alle Performance-Tests
```bash
./mvnw test -Dtest=MongoDBPerformanceTest,MongoDBIndexPerformanceTest
```

### Nur CRUD-Tests
```bash
./mvnw test -Dtest=MongoDBPerformanceTest
```

### Nur Index-Tests
```bash
./mvnw test -Dtest=MongoDBIndexPerformanceTest
```

### Einzelne Tests
```bash
./mvnw test -Dtest=MongoDBPerformanceTest#testWritingOperations_Aerzte
```

---

## 📊 Performance-Analyse

### MongoDB Vorteile
- ✅ **Schnelle Writes**: Keine referentielle Integrität
- ✅ **Einfache Reads**: Embedded Docs, keine JOINs
- ✅ **Flexible Schema**: Einfache Änderungen
- ✅ **Horizontal skalierbar**: Sharding

### MongoDB Nachteile
- ❌ **Daten-Redundanz**: Bei Denormalisierung
- ❌ **Komplexe Transaktionen**: Multi-Document
- ❌ **Konsistenz**: Eventually Consistent

### Wann MongoDB?
- ✅ Hoher Read-Throughput
- ✅ Flexible Schema-Anforderungen
- ✅ Horizontal skalieren muss
- ✅ JSON-ähnliche Datenstrukturen

### Wann Relational?
- ✅ Komplexe Transaktionen
- ✅ Starke Konsistenz-Anforderungen
- ✅ Normalisierte Daten
- ✅ Komplexe JOINs & Reports

---

## 🔍 Troubleshooting

### MongoDB startet nicht
```bash
docker ps | findstr mongo
docker logs mongo
docker-compose restart mongo
```

### Port 27017 belegt
```bash
# Port prüfen
netstat -ano | findstr :27017

# Alternative Port verwenden
# In application.properties:
spring.data.mongodb.uri=mongodb://localhost:27018/patientenverwaltungdb
```

### Tests schlagen fehl
```bash
# Datenbank leeren
mongosh patientenverwaltungdb --eval "db.dropDatabase()"
```

### OutOfMemory
```bash
# Mehr Heap-Speicher
set MAVEN_OPTS=-Xmx2g
./mvnw test
```

---

## 👥 Team

- **Zeller**
- **Van de Castell**

**Projekt:** Datenbank-Performance-Vergleich  
**Kurs:** Datenbanken & Informationssysteme  
**Datum:** November 2025

---

## 📝 Lizenz

Dieses Projekt ist Teil eines Universitätsprojekts.

---

## 🎓 Weitere Ressourcen

- [MongoDB Official Docs](https://docs.mongodb.com/)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [MongoDB Performance Best Practices](https://www.mongodb.com/docs/manual/administration/analyzing-mongodb-performance/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

---

## 🚀 Nächste Schritte

1. ✅ MongoDB-Implementierung abgeschlossen
2. ✅ Performance-Tests implementiert
3. ⏳ **Tests ausführen und Ergebnisse dokumentieren** ← SIE SIND HIER
4. ⏳ Vergleich mit relationaler DB
5. ⏳ Optional: Weitere Bonus-Features
6. ⏳ Abgabe vorbereiten

---

<div align="center">

**Made with ❤️ by Zeller & Van de Castell**

[Dokumentation](MONGODB_PERFORMANCE_README.md) • [Quick Start](QUICKSTART.md) • [Checkliste](ABGABE_CHECKLISTE.md)

</div>

