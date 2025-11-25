package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByNameIgnoreCase(String name);
    List<Organization> findByNameContainingIgnoreCase(String q);
}