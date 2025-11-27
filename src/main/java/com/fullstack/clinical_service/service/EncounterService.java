package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Encounter;
import com.fullstack.clinical_service.entity.Patient;
import com.fullstack.clinical_service.entity.Practitioner;
import com.fullstack.clinical_service.entity.Location;
import com.fullstack.clinical_service.repository.EncounterRepository;
import com.fullstack.clinical_service.repository.PatientRepository;
import com.fullstack.clinical_service.repository.PractitionerRepository;
import com.fullstack.clinical_service.repository.LocationRepository;
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EncounterService {

    private final EncounterRepository encounterRepository;
    private final PatientRepository patientRepository;
    private final PractitionerRepository practitionerRepository;
    private final LocationRepository locationRepository;

    public EncounterService(
            EncounterRepository encounterRepository,
            PatientRepository patientRepository,
            PractitionerRepository practitionerRepository,
            LocationRepository locationRepository) {
        this.encounterRepository = encounterRepository;
        this.patientRepository = patientRepository;
        this.practitionerRepository = practitionerRepository;
        this.locationRepository = locationRepository;
    }

    public List<Encounter> getAllEncounters() {
        return encounterRepository.findAll();
    }

    public Optional<Encounter> getEncounterById(Long id) {
        return encounterRepository.findById(id);
    }

    public List<Encounter> getEncountersByPatientId(Long patientId) {
        return encounterRepository.findByPatient_Id(patientId);
    }

    public Encounter createEncounter(Encounter encounter) {
        // 1. Koppla Patient
        if (encounter.getPatient() == null && encounter.getPatientId() != null) {
            Patient p = patientRepository.findById(encounter.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + encounter.getPatientId()));
            encounter.setPatient(p);
        } else if (encounter.getPatient() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }

        // 2. Koppla Practitioner (Läkare/Personal)
        if (encounter.getPractitioner() == null && encounter.getPractitionerId() != null) {
            Practitioner pr = practitionerRepository.findById(encounter.getPractitionerId())
                    .orElse(null); // Vi tillåter null om den inte hittas, eller kasta exception om du föredrar
            encounter.setPractitioner(pr);
        }

        // 3. Koppla Location (Plats/Rum)
        if (encounter.getLocation() == null && encounter.getLocationId() != null) {
            Location loc = locationRepository.findById(encounter.getLocationId())
                    .orElse(null);
            encounter.setLocation(loc);
        }

        return encounterRepository.save(encounter);
    }

    public Encounter updateEncounter(Long id, Encounter updated) {
        Encounter existing = encounterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Encounter not found"));

        existing.setEncounterDate(updated.getEncounterDate());
        existing.setDiagnosis(updated.getDiagnosis());
        existing.setNotes(updated.getNotes());

        // Här skulle man kunna lägga till logik för att uppdatera plats/läkare också om man vill

        return encounterRepository.save(existing);
    }

    public void deleteEncounter(Long id) {
        encounterRepository.deleteById(id);
    }
}
