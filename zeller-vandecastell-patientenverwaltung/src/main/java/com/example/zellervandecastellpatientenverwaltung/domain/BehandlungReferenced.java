package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Behandlung mit @DBRef (Referencing statt Embedding)
 *
 * BONUS-AUFGABE: Vergleich Referencing vs Embedding (1.0 Punkt)
 *
 * Diese Variante verwendet @DBRef für Arzt und Patient.
 * Das bedeutet: Nur die ObjectIds werden gespeichert, nicht die kompletten Objekte.
 *
 * Vorteile:
 * - Konsistenz: Updates von Arzt/Patient werden automatisch reflektiert
 * - Kleinere Dokumente: Weniger Redundanz
 * - Normalisierung wie in relationalen DBs
 *
 * Nachteile:
 * - Langsamere Reads: Erfordert zusätzliche Queries (ähnlich JOIN)
 * - Komplexere Queries
 * - Netzwerk-Overhead
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "behandlungen_referenced")
public class BehandlungReferenced {

    @Id
    private String id;

    /**
     * @DBRef: Speichert nur die ObjectId des Arztes
     * MongoDB lädt das Objekt bei Bedarf nach (Lazy Loading möglich)
     */
    @NotNull
    @DBRef
    @Field("arztRef")
    private Arzt arzt;

    /**
     * @DBRef: Speichert nur die ObjectId des Patienten
     */
    @NotNull
    @DBRef
    @Field("patientRef")
    private Patient patient;

    @Field("behandlungsraumId")
    private String behandlungsraumId;

    /**
     * Medikamente bleiben embedded - macht Sinn für kleine, behandlungsspezifische Daten
     */
    @Field("medikamente")
    private List<Medikament> medikamente;

    @Field("beginn")
    private LocalDateTime beginn;

    @Field("ende")
    private LocalDateTime ende;

    @Field("diagnose")
    private String diagnose;

    @Field("apiKey")
    private String apiKey;
}

