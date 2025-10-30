package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BehandlungRepository extends MongoRepository<Behandlung, String> {
    Optional<Behandlung> findByDiagnoseIgnoreCase(String diagnose);
    List<Behandlung> findByDiagnoseContainingIgnoreCase(String diagnose);
}
