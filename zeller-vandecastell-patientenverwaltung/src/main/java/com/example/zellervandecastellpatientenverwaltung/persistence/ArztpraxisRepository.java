package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Arztpraxis;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ArztpraxisRepository extends MongoRepository<Arztpraxis, String> {
    Optional<Arztpraxis> findByNameIgnoreCase(String name);
}
