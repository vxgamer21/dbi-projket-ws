package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "behandlungen")
public class Behandlung{
    @EmbeddedId
    private BehandlungId behandlungId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @NotNull
    private Arzt arzt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Patient_2_Behandlung"))
    @NotNull
    private Patient patient;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Behandlungsraum_2_Behandlung"))
    private Behandlungsraum behandlungsraum;

    @ElementCollection
    @JoinTable(name = "medikamente",joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_Medikament_2_Behandlung")))
    private List<Medikament> medikamente;

    private LocalDateTime beginn;
    private LocalDateTime ende;
    private String diagnose;

    private String apiKey;

    public record BehandlungId(@GeneratedValue @NotNull Long id) {}

    public Long getId() {
        return behandlungId != null ? behandlungId.id() : null;
    }
    public void setId(Long id) {
        if (behandlungId == null) {
            this.behandlungId = new BehandlungId(id);
        } else {
            this.behandlungId = new BehandlungId(id);
        }
    }

}