package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Encounter;
import com.fullstack.clinical_service.repository.EncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EncounterService {

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    public List<Encounter> getAllEncounters() {
        return encounterRepository.findAll();
    }

    public Optional<Encounter> getEncounterById(Long id) {
        return encounterRepository.findById(id);
    }

    public List<Encounter> getEncountersByPatient(Long patientId) {
        return encounterRepository.findByPatientId(patientId);
    }

    public List<Encounter> getEncountersByPractitioner(Long practitionerId) {
        return encounterRepository.findByPractitionerId(practitionerId);
    }

    public List<Encounter> getEncountersByPractitionerAndDate(Long practitionerId, LocalDate date) {
        return encounterRepository.findByPractitionerIdAndEncounterDate(practitionerId, date);
    }

    public Encounter createEncounter(Long patientId, Encounter encounter) {
        // Verifiera att patient finns
        verifyPatientExists(patientId);

        encounter.setPatientId(patientId);
        return encounterRepository.save(encounter);
    }

    public Encounter updateEncounter(Long id, Encounter updatedEncounter) {
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encounter not found"));

        encounter.setEncounterDate(updatedEncounter.getEncounterDate());
        encounter.setDiagnosis(updatedEncounter.getDiagnosis());
        encounter.setNotes(updatedEncounter.getNotes());

        return encounterRepository.save(encounter);
    }

    public void deleteEncounter(Long id) {
        if (!encounterRepository.existsById(id)) {
            throw new RuntimeException("Encounter not found");
        }
        encounterRepository.deleteById(id);
    }

    // ✅ Använd Object istället för PatientDTO
    private void verifyPatientExists(Long patientId) {
        String url = patientServiceUrl + "/api/patients/" + patientId;
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        } catch (Exception e) {
            throw new RuntimeException("Error verifying patient: " + e.getMessage());
        }
    }
}
