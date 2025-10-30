package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "rechnung")
public class Rechnung {

    @EmbeddedId
    private RechnungId rechnungId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Rechnung_2_Patient"))
    @NotNull
    private Patient patient;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Rechnung_2_Arzt"))
    @NotNull
    private Arzt arzt;

    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "10000.0", inclusive = true)
    @NotNull
    private Double betrag;
    private boolean bezahlt;
    private Zahlungsart zahlungsart;
    private LocalDateTime datum;

    @NotNull
    private String apiKey;

    public record RechnungId(@GeneratedValue @NotNull Long id) {}
}
