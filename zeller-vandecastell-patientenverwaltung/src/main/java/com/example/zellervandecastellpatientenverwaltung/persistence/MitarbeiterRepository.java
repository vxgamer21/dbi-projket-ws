package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Mitarbeiter;
import com.example.zellervandecastellpatientenverwaltung.dtos.MitarbeiterDtos;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface MitarbeiterRepository extends MongoRepository<Mitarbeiter, String> {
    Optional<Mitarbeiter> findByNameIgnoreCase(String name);
    List<Mitarbeiter> findByNameContainingIgnoreCase(String name);
    List<MitarbeiterDtos.Minimal> findAllMinimalBy();

}
