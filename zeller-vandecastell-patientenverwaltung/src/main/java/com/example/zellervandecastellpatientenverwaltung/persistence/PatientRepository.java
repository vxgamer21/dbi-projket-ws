package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByNameIgnoreCase(String name);
    Optional<Patient> findById(String id);
    List<Patient> findByNameContainingIgnoreCase(String name);
}
