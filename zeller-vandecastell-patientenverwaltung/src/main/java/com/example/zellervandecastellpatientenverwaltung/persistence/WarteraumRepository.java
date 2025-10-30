package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Warteraum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarteraumRepository extends MongoRepository<Warteraum, String> {
    Optional<Warteraum> findById(String id);
}
