package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Observation;
import com.fullstack.clinical_service.entity.Encounter;
import com.fullstack.clinical_service.repository.ObservationRepository;
import com.fullstack.clinical_service.repository.EncounterRepository; // NYTT
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final EncounterRepository encounterRepository; // NYTT

    public ObservationService(ObservationRepository observationRepository, EncounterRepository encounterRepository) {
        this.observationRepository = observationRepository;
        this.encounterRepository = encounterRepository;
    }

    public List<Observation> getAllObservations() {
        return observationRepository.findAll();
    }

    public Optional<Observation> getObservationById(Long id) {
        return observationRepository.findById(id);
    }

    public List<Observation> getObservationsByEncounterId(Long encounterId) {
        return observationRepository.findByEncounter_Id(encounterId);
    }

    public Observation createObservation(Observation observation) {
        // Koppla encounter via ID om det finns
        if (observation.getEncounter() == null && observation.getEncounterId() != null) {
            Encounter e = encounterRepository.findById(observation.getEncounterId())
                    .orElseThrow(() -> new NotFoundException("Encounter not found with ID: " + observation.getEncounterId()));
            observation.setEncounter(e);
        } else if (observation.getEncounter() == null) {
            throw new IllegalArgumentException("Encounter ID is required");
        }

        return observationRepository.save(observation);
    }

    public Observation updateObservation(Long id, Observation updated) {
        Observation existing = observationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Observation not found"));

        existing.setObservationType(updated.getObservationType());
        existing.setValue(updated.getValue());
        existing.setUnit(updated.getUnit());
        // Om man vill kunna ändra datum:
        if (updated.getObservedAt() != null) {
            existing.setObservedAt(updated.getObservedAt());
        }

        return observationRepository.save(existing);
    }

    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
    }
}
