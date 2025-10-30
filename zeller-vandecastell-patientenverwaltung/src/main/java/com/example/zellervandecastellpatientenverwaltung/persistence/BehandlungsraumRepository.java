package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlungsraum;
import com.example.zellervandecastellpatientenverwaltung.domain.Raum;
import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungsRaumDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungsRaumDtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BehandlungsraumRepository extends JpaRepository<Behandlungsraum, Behandlungsraum.BehandlungsraumId> {
    Optional<Behandlungsraum> findById(Behandlungsraum.BehandlungsraumId raumId);
    List<Behandlungsraum> findByNameContainingIgnoreCase(String name);

    @Query("select distinct b from Behandlungsraum b join fetch b.behandlungen")
    List<BehandlungsRaumDto> findAllProjectedBy();

    List<BehandlungsRaumDtos.Minimal> findAllMinimalBy();
}
