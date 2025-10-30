package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Raum;
import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import com.example.zellervandecastellpatientenverwaltung.dtos.WarteraumDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarteraumRepository extends JpaRepository<Warteraum, Warteraum.WarteraumId> {
    Optional<Warteraum> findById(Warteraum.WarteraumId raumId);

    @Query("select new com.example.zellervandecastellpatientenverwaltung.dtos.WarteraumDto(w) from Warteraum w")
    List<WarteraumDto> findAllProjectedBy();



}
