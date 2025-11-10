package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.BehandlungReferenced;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository für Behandlungen mit @DBRef (Referencing)
 */
@Repository
public interface BehandlungReferencedRepository extends MongoRepository<BehandlungReferenced, String> {

    List<BehandlungReferenced> findByBeginnAfter(LocalDateTime date);

    List<BehandlungReferenced> findByDiagnose(String diagnose);

    long countByArztId(String arztId);
}

