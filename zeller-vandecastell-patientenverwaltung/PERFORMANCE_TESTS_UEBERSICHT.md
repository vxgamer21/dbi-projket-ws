# SQL Performance Tests - Übersicht

## ✅ Implementiert

Die SQL Performance Tests für die relationale Datenbank (PostgreSQL) wurden erfolgreich implementiert.

## 📁 Erstellte Dateien

### Test-Klassen
1. **SQLPerformanceTest.java**
   - Pfad: `src/test/java/com/example/zellervandecastellpatientenverwaltung/performance/`
   - Grundlegende Performance-Tests
   - Alle geforderten Operationen abgedeckt

2. **SQLDetailedPerformanceTest.java**
   - Pfad: `src/test/java/com/example/zellervandecastellpatientenverwaltung/performance/`
   - Erweiterte Tests mit detaillierter Auswertung
   - Übersichtliche Statistiken und Reports

### Dokumentation
3. **README_SQL_PERFORMANCE.md**
   - Pfad: `src/test/java/com/example/zellervandecastellpatientenverwaltung/performance/`
   - Ausführliche Dokumentation
   - Beispielausgaben und Metriken

### Scripts
4. **run-sql-performance-tests.bat**
   - Pfad: Projekt-Root
   - Windows Batch-Script zum einfachen Ausführen der Tests

## 📋 Getestete Operationen

### ✅ WRITE Operations (2)
- ✓ **Write 1:** Patienten erstellen (100, 1.000, 10.000 Datensätze)
- ✓ **Write 2:** Behandlungen erstellen (100, 1.000, 10.000 Datensätze)

### ✅ FIND Operations (4)
- ✓ **Find 1:** Alle Datensätze ohne Filter (`SELECT *`)
- ✓ **Find 2:** Mit Filter (`WHERE ... LIKE`)
- ✓ **Find 3:** Mit Filter und Projektion (ausgewählte Spalten)
- ✓ **Find 4:** Mit Filter, Projektion und Sortierung (komplexe JOINs)

### ✅ UPDATE Operation (1)
- ✓ **Update:** Massendaten aktualisieren (1.000 Datensätze)

### ✅ DELETE Operation (1)
- ✓ **Delete:** Massendaten löschen (1.000 Datensätze)

## 🚀 Tests ausführen

### Option 1: Batch-Script (Windows)
```cmd
run-sql-performance-tests.bat
```

### Option 2: Maven direkt
```cmd
# Einfacher Test
mvnw test -Dtest=SQLPerformanceTest

# Detaillierter Test mit Report
mvnw test -Dtest=SQLDetailedPerformanceTest

# Beide Tests
mvnw test -Dtest=SQL*PerformanceTest
```

## 📊 Test-Metriken

Die Tests messen:
- ⏱️ **Ausführungszeit** in Millisekunden
- 📈 **Durchsatz** (Datensätze pro Sekunde)
- 🔢 **Anzahl verarbeiteter Datensätze**
- 📏 **Skalierungsverhalten**

## 🔧 Voraussetzungen

### Datenbank
- PostgreSQL muss laufen
- Database: `patientenverwaltung`
- User: `patientenverwaltungowner`
- Password: `patientenverwaltungowner`
- Port: 5432

### Konfiguration
Die `application.properties` muss korrekt konfiguriert sein:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/patientenverwaltung
spring.datasource.username=patientenverwaltungowner
spring.datasource.password=patientenverwaltungowner
spring.datasource.driver-class-name=org.postgresql.Driver
```

## 📝 Test-Ergebnisse

Die Testergebnisse werden in folgenden Formaten ausgegeben:

1. **Console Output:** Detaillierte Ausgabe während der Testausführung
2. **Surefire Reports:** XML und Text Reports in `target/surefire-reports/`
3. **Zusammenfassung:** Übersichtliche Statistiken am Ende der Tests

## 🎯 Erfüllte Anforderungen

Gemäß den Projektanforderungen wurden folgende Punkte umgesetzt:

✅ **2 Writing Operations** in verschiedenen Skalierungen (100, 1.000, 10.000)
✅ **4 Find Operations:**
   - Ohne Filter
   - Mit Filter
   - Mit Filter und Projektion
   - Mit Filter, Projektion und Sortierung
✅ **1 Update Operation**
✅ **1 Delete Operation**

## 🔄 Nächste Schritte

Für den vollständigen Vergleich fehlt noch:
- [ ] MongoDB Performance Tests (analog zu SQL Tests)
- [ ] Vergleichsanalyse zwischen SQL und MongoDB
- [ ] Visualisierung der Ergebnisse

## 💡 Hinweise

- Die Tests verwenden `@DirtiesContext` für saubere Testumgebungen
- Tests sind mit `@Order` annotiert für deterministische Ausführung
- Zeitmessung erfolgt mit `System.nanoTime()` für höhere Präzision
- Batch-Operationen verwenden `saveAll()` für bessere Performance

## 📚 Weitere Informationen

Siehe `README_SQL_PERFORMANCE.md` im Performance-Test-Ordner für:
- Detaillierte Beispielausgaben
- Erweiterungsmöglichkeiten
- Best Practices

