package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ArztRepository extends MongoRepository<Arzt, String> {
    Optional<Arzt> findByNameIgnoreCase(String name);
    Optional<Arzt> findById(String id);

    List<Arzt> findAll();
}
