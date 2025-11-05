package com.example.zellervandecastellpatientenverwaltung.persistence;

import com.example.zellervandecastellpatientenverwaltung.domain.Arzt;
import com.example.zellervandecastellpatientenverwaltung.domain.User;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztDto;
import com.example.zellervandecastellpatientenverwaltung.dtos.ArztDtos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArztRepository extends JpaRepository<Arzt, Arzt.ArztId> {
    Optional<Arzt> findByNameIgnoreCase(String name);
    Optional<Arzt> findById(Arzt.ArztId id);
    List<Arzt> findByNameContainingIgnoreCase(String name);

    List<ArztDto> findAllProjectedBy();

    List<ArztDtos.Minimal> findAllMinimalBy();
}

