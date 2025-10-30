package com.example.zellervandecastellpatientenverwaltung.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "arztpraxen")
public class Arztpraxis{
    @EmbeddedId
    private ArztpraxisId arztpraxisId;

    @NotNull
    private String name;
    private Boolean istKassenarzt;

    private Adresse adresse;

    @Embedded
    private TelefonNummer telefonNummer;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Arztpraxis_2_Arzt"))
    private List<Arzt> aerzte;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Arztpraxis_2_Mitarbeiter"))
    private List<Mitarbeiter> mitarbeiter;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Arztpraxis_2_Behandlungsraum"))
    private List<Behandlungsraum> behandlungsraum;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Arztpraxis_2_Warteraum"))
    private List<Warteraum> warteraum;

    @NotNull
    private String apiKey;

    public record ArztpraxisId(@GeneratedValue @NotNull Long id) {}
}
