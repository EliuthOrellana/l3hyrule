package com.uca.labo3hyrule.repositories;

import com.uca.labo3hyrule.domain.entities.Specimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecimenRepository extends JpaRepository<Specimen, UUID> {
}

