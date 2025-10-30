package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Mitarbeiter;
import com.example.zellervandecastellpatientenverwaltung.domain.User;
import com.example.zellervandecastellpatientenverwaltung.dtos.MitarbeiterDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.MitarbeiterDtos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Mitarbeiter.MitarbeiterID> {
    Optional<Mitarbeiter> findByNameIgnoreCase(String name);
    List<Mitarbeiter> findByNameContainingIgnoreCase(String name);

    List<MitarbeiterDtos.Minimal> findAllMinimalBy();
    List<MitarbeiterDto> findAllProjectedBy();

}
