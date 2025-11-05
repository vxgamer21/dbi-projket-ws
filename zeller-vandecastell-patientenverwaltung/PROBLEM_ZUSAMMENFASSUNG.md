# 🚨 Problem-Zusammenfassung: SQL Performance Tests

## Status: ⚠️ Tests können NICHT ausgeführt werden

### Hauptproblem: PostgreSQL ENUM-Typ Inkompatibilität

Die Entities `Arzt` und `Patient` verwenden MySQL-ENUM-Syntax, die PostgreSQL nicht unterstützt:

```java
// In Arzt.java:
@Column(name = "fachgebiet", columnDefinition = "enum('O','H','C','G','A')", nullable = false)
private Fachgebiet fachgebiet;

// In Patient.java:
@Column(name = "versicherungsart", columnDefinition = "enum ('K','P')")
private Versicherungsart versicherungsart;
```

**Fehler:**
```
ERROR: type "enum" does not exist
Position: 44
```

## Ursache

- **MySQL**: Unterstützt `ENUM('wert1', 'wert2')` direkt in der CREATE TABLE-Syntax
- **PostgreSQL**: Benötigt zuerst `CREATE TYPE typ_name AS ENUM (...)` und dann Verwendung dieses Typs

## Lösungsmöglichkeiten

### Option 1: ✅ Einfach - Verwende VARCHAR statt ENUM (EMPFOHLEN)

Ändere die Entities:

```java
// Arzt.java
@Column(name = "fachgebiet", length = 1, nullable = false)
@Enumerated(EnumType.STRING)
private Fachgebiet fachgebiet;

// Patient.java
@Column(name = "versicherungsart", length = 1)
@Enumerated(EnumType.STRING)
private Versicherungsart versicherungsart;
```

**Vorteile:**
- Funktioniert mit beiden Datenbanken (MySQL & PostgreSQL)
- Keine Migration nötig
- Schnell zu implementieren

### Option 2: ⚙️ Mittel - PostgreSQL ENUM Types erstellen

Erstelle die ENUM-Typen vor dem Schema-Erstellen:

```sql
-- In Flyway Migration oder schema.sql
CREATE TYPE fachgebiet_enum AS ENUM ('O', 'H', 'C', 'G', 'A');
CREATE TYPE versicherungsart_enum AS ENUM ('K', 'P');
```

Dann in den Entities:
```java
@Column(name = "fachgebiet", columnDefinition = "fachgebiet_enum", nullable = false)
private Fachgebiet fachgebiet;
```

**Vorteile:**
- Nutzt native PostgreSQL-Features
- Bessere Datenintegrität

**Nachteile:**
- Funktioniert nicht mehr mit MySQL
- Benötigt zusätzliche Migration

### Option 3: 🔧 Komplex - Datenbankspezifische Konfiguration

Verwende `@Column(columnDefinition = "...")` nur für MySQL und Standardmapping für PostgreSQL.

**Nachteile:**
- Erhöhte Komplexität
- Schwer wartbar

## Empfohlene Lösung für Performance Tests

**Für schnelle Performance-Tests:**

1. **Entferne die `columnDefinition` aus den Entities:**
   - `Arzt.java` → Zeile mit `columnDefinition = "enum(...)"`
   - `Patient.java` → Zeile mit `columnDefinition = "enum(...)"`

2. **Verwende `@Enumerated(EnumType.STRING)`:**
   ```java
   @Enumerated(EnumType.STRING)
   @Column(name = "fachgebiet", length = 1, nullable = false)
   private Fachgebiet fachgebiet;
   ```

3. **Tests ausführen:**
   ```cmd
   .\mvnw.cmd test -Dtest=SQLPerformanceTest
   ```

## Was wurde bereits erfolgreich implementiert

✅ **SQLPerformanceTest.java** - Vollständig implementiert:
   - 8 Test-Methoden mit @Order
   - WRITE Tests (100, 1.000, 10.000 Datensätze)
   - FIND Tests (4 Varianten)
   - UPDATE Test (1.000 Datensätze)
   - DELETE Test (1.000 Datensätze)

✅ **Testcontainers-Integration** - Funktioniert:
   - PostgreSQL Container wird gestartet
   - Verbindung wird aufgebaut
   - Hibernate versucht Schema zu erstellen

✅ **Dokumentation** - Vollständig:
   - README_SQL_PERFORMANCE.md
   - PERFORMANCE_TESTS_UEBERSICHT.md  
   - ANLEITUNG_TEST_AUSFUEHRUNG.md

## Nächste Schritte

1. **Entscheide** welche Lösungsoption du verwenden möchtest
2. **Passe** die Entities entsprechend an
3. **Führe** die Tests aus
4. **Analysiere** die Performance-Ergebnisse

---

**Erstellt:** 2025-11-05  
**Status:** Blockiert durch ENUM-Inkompatibilität

