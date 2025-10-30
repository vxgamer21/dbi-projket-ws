package com.example.zellervandecastellpatientenverwaltung.dtos;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlungsraum;
import java.util.List;

public record BehandlungsRaumDto(String ausstattung, boolean isFrei, List<BehandlungDto> behandlungen) {

    public BehandlungsRaumDto(Behandlungsraum b) {
        this(
                b.getAusstattung(),
                b.getIsFrei(),
                b.getBehandlungen()
                        .stream()
                        .map(BehandlungDto::new)
                        .toList()
        );
    }
}
