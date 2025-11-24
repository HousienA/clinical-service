package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    Optional<Practitioner> findByEmail(String email);
    List<Practitioner> findByFullNameContainingIgnoreCase(String name);
    List<Practitioner> findBySpecialization(String specialization);
}