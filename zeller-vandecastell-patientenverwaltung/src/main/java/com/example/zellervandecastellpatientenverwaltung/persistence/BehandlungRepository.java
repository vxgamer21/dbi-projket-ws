package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Behandlung;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungDtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BehandlungRepository extends JpaRepository<Behandlung, Behandlung.BehandlungId> {
    Optional<Behandlung> findByDiagnoseIgnoreCase(String diagnose);
    List<Behandlung> findByDiagnoseContainingIgnoreCase(String diagnose);

    List<BehandlungDtos.Minimal> findAllMinimalBy();

    @Query("""
    select new com.example.zellervandecastellpatientenverwaltung.dtos.BehandlungDto(
        b.beginn,
        b.ende,
        b.diagnose,
        new com.example.zellervandecastellpatientenverwaltung.dtos.ArztDto(b.arzt),
        new com.example.zellervandecastellpatientenverwaltung.dtos.PatientDto(b.patient)
    )
    from Behandlung b
    """)
    List<BehandlungDto> findAllProjectedBy();

}
