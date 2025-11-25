package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Observation;
import com.fullstack.clinical_service.repository.ObservationRepository;
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObservationService {

    private final ObservationRepository observationRepository;

    public ObservationService(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    public List<Observation> getAllObservations() {
        return observationRepository.findAll();
    }

    public Optional<Observation> getObservationById(Long id) {
        return observationRepository.findById(id);
    }

    public List<Observation> getObservationsByEncounterId(Long encounterId) {
        return observationRepository.findByEncounterId(encounterId);
    }

    public Observation createObservation(Observation observation) {
        // Här antas det att JSON-objektet har ett giltigt "encounter": { "id": 1 }
        // JPA hanterar kopplingen automatiskt om ID:t finns.
        return observationRepository.save(observation);
    }

    public Observation updateObservation(Long id, Observation updated) {
        Observation existing = observationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Observation not found"));

        existing.setObservationType(updated.getObservationType());
        existing.setValue(updated.getValue());
        existing.setUnit(updated.getUnit());

        return observationRepository.save(existing);
    }

    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
    }
}
