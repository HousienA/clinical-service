package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByOrganizationId(Long organizationId);
    List<Location> findByNameContainingIgnoreCase(String q);
    List<Location> findByOrganization_IdAndNameContainingIgnoreCase(Long orgId, String q);
}
