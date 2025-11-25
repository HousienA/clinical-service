package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Organization;
import com.fullstack.clinical_service.repository.OrganizationRepository;
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
    }

    public List<Organization> search(String query) {
        if (query != null && !query.isBlank()) {
            return organizationRepository.findByNameContainingIgnoreCase(query);
        }
        return getAllOrganizations();
    }

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization updateOrganization(Long id, Organization updated) {
        Organization existing = organizationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        existing.setName(updated.getName());
        return organizationRepository.save(existing);
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }
}
