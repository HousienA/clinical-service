package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    List<Encounter> findByPatient_Id(Long patientId);

    List<Encounter> findByPractitioner_Id(Long practitionerId);

    List<Encounter> findByPractitioner_IdAndEncounterDateBetween(Long practitionerId, java.time.LocalDateTime start,
            java.time.LocalDateTime end);
}
