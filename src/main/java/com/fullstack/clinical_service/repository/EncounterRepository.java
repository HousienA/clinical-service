package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    List<Encounter> findByPatientId(Long patientId);
    List<Encounter> findByPractitionerId(Long practitionerId);
    List<Encounter> findByPractitionerIdAndEncounterDate(Long practitionerId, LocalDate date);
}