package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Observation;
import com.fullstack.clinical_service.repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    public List<Observation> getAllObservations() {
        return observationRepository.findAll();
    }

    public Optional<Observation> getObservationById(Long id) {
        return observationRepository.findById(id);
    }

    public List<Observation> getObservationsByPatient(Long patientId) {
        return observationRepository.findByPatientId(patientId);
    }

    public List<Observation> getObservationsByEncounter(Long encounterId) {
        return observationRepository.findByEncounterId(encounterId);
    }

    public Observation createObservation(Observation observation) {
        return observationRepository.save(observation);
    }

    public Observation updateObservation(Long id, Observation updatedObservation) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Observation not found"));

        observation.setObservationType(updatedObservation.getObservationType());
        observation.setValue(updatedObservation.getValue());
        observation.setUnit(updatedObservation.getUnit());
        observation.setNotes(updatedObservation.getNotes());

        return observationRepository.save(observation);
    }

    public void deleteObservation(Long id) {
        if (!observationRepository.existsById(id)) {
            throw new RuntimeException("Observation not found");
        }
        observationRepository.deleteById(id);
    }
}