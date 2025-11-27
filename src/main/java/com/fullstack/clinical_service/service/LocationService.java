package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Location;
import com.fullstack.clinical_service.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> search(Long orgId, String query) {
        if (orgId != null && query != null) {
            return locationRepository.findByOrganization_IdAndNameContainingIgnoreCase(orgId, query);
        } else if (orgId != null) {
            return locationRepository.findByOrganizationId(orgId);
        } else if (query != null) {
            return locationRepository.findByNameContainingIgnoreCase(query);
        }
        return getAllLocations();
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }
}
