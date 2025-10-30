package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Arztpraxis;

import com.example.zellervandecastellpatientenverwaltung.dtos.ArztpraxisDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztpraxisDtos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArztpraxisRepository extends JpaRepository<Arztpraxis, Arztpraxis.ArztpraxisId> {
    Optional<Arztpraxis> findByNameIgnoreCase(String name);

//    List<ArztpraxisDto> findAllProjectedBy();
//    List<ArztpraxisDtos.Minimal> findAllMinimalBy();
}
