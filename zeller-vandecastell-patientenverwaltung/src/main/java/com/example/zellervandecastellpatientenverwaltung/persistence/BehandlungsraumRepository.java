package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlungsraum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BehandlungsraumRepository extends MongoRepository<Behandlungsraum, String> {
    Optional<Behandlungsraum> findById(String id);
    List<Behandlungsraum> findByNameContainingIgnoreCase(String name);
}
