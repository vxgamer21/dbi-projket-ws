package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Rechnung;
import com.example.zellervandecastellpatientenverwaltung.dtos.RechnungDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.RechnungDtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RechnungRepository extends JpaRepository<Rechnung, Rechnung.RechnungId>
{
    Optional<Rechnung> findById(Rechnung.RechnungId rechnungId);

    @Query("""
       SELECT new com.example.zellervandecastellpatientenverwaltung.dtos.RechnungDto(
           new com.example.zellervandecastellpatientenverwaltung.dtos.PatientDto(p.name, p.gebDatum, p.svnr, p.versicherungsart),
           new com.example.zellervandecastellpatientenverwaltung.dtos.ArztDto(a.name, a.gebDatum, a.svnr),
           r.betrag, r.bezahlt, r.datum) 
       FROM Rechnung r 
       JOIN r.patient p 
       JOIN r.arzt a
       """)
    List<RechnungDto> findAllProjectedBy();



    @Query(value = "SELECT r.id, r.betrag, r.bezahlt FROM rechnung r", nativeQuery = true)
    List<RechnungDtos.Minimal> findAllMinimalBy();




}
