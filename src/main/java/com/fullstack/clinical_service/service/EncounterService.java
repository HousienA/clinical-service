package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Encounter;
import com.fullstack.clinical_service.repository.EncounterRepository;
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EncounterService {
    private final EncounterRepository encounterRepository;

    public EncounterService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    public List<Encounter> getAllEncounters() {
        return encounterRepository.findAll();
    }

    public Optional<Encounter> getEncounterById(Long id) {
        return encounterRepository.findById(id);
    }

    public List<Encounter> getEncountersByPatientId(Long patientId) {
        return encounterRepository.findByPatientId(patientId);
    }

    public Encounter createEncounter(Encounter encounter) {
        // Här kan man lägga logik för att hämta patient/läkare från databasen
        // om man bara skickat med deras ID i JSON-objektet.
        return encounterRepository.save(encounter);
    }

    public Encounter updateEncounter(Long id, Encounter updated) {
        Encounter existing = encounterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Encounter not found"));

        existing.setEncounterDate(updated.getEncounterDate());
        existing.setDiagnosis(updated.getDiagnosis());
        existing.setNotes(updated.getNotes());
        // Uppdatera relationer om nödvändigt

        return encounterRepository.save(existing);
    }

    public void deleteEncounter(Long id) {
        encounterRepository.deleteById(id);
    }
}
