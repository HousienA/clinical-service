package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPersonalNumber(String personalNumber);

    List<Patient> findByPersonalNumberContainingIgnoreCase(String pnr);
    List<Patient> findByFullNameContainingIgnoreCase(String name);
    List<Patient> findByPersonalNumberContainingIgnoreCaseAndFullNameContainingIgnoreCase(String pnrPart, String namePart);
    List<Patient> findByPrimaryPractitionerId(Long practitionerId);


}