package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Patient;
import com.example.zellervandecastellpatientenverwaltung.dtos.PatientDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.PatientDtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Patient.PatientID> {
    Optional<Patient> findByNameIgnoreCase(String name);
    Optional<Patient> findById(Patient.PatientID id);
    List<Patient> findByNameContainingIgnoreCase(String name);

    List<PatientDto> findAllProjectedBy();
    List<PatientDtos.Minimal> findAllMinimalBy();
}
