package com.example.zellervandecastellpatientenverwaltung.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Raum implements Serializable {

    @NotNull
    private String name;
}
