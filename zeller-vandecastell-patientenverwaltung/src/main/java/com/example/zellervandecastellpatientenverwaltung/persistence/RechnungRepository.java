package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface RechnungRepository extends MongoRepository<Rechnung, String> {
    Optional<Rechnung> findById(String id);
    List<Rechnung> findByBezahlt(boolean bezahlt);
}
