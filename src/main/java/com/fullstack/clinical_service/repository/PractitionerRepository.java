package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {

    Optional<Practitioner> findByAuthId(String authId);
}
