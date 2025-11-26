package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Patient;
import com.fullstack.clinical_service.entity.Practitioner;
import com.fullstack.clinical_service.enums.Result;
import com.fullstack.clinical_service.exception.ApiException;
import com.fullstack.clinical_service.exception.NotFoundException;
import com.fullstack.clinical_service.repository.PatientRepository;
import com.fullstack.clinical_service.repository.PractitionerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final PractitionerRepository practitionerRepository;

    public PatientService(PatientRepository patientRepository, PractitionerRepository practitionerRepository) {
        this.patientRepository = patientRepository;
        this.practitionerRepository = practitionerRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Tar emot en Entitet direkt
    public Result createPatient(Patient patient) {
        if (patient.getFullName() == null || patient.getFullName().isBlank()) {
            throw new ApiException("Full name is required");
        }

        // Kolla om vi fick med ett practitioner-ID i JSON-objektet, även om det är en entitet
        if (patient.getPrimaryPractitioner() != null && patient.getPrimaryPractitioner().getId() != null) {
            Practitioner p = practitionerRepository.findById(patient.getPrimaryPractitioner().getId())
                    .orElseThrow(() -> new ApiException("Practitioner not found"));
            patient.setPrimaryPractitioner(p);
        }

        patientRepository.save(patient);
        return Result.SUCCESS;
    }

    public Result updatePatient(Long id, Patient updatedData) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        existing.setFullName(updatedData.getFullName());
        existing.setPersonalNumber(updatedData.getPersonalNumber());
        existing.setEmail(updatedData.getEmail());
        existing.setPhone(updatedData.getPhone());

        // Uppdatera authId om det skickas med
        if (updatedData.getAuthId() != null) {
            existing.setAuthId(updatedData.getAuthId());
        }

        patientRepository.save(existing);
        return Result.SUCCESS;
    }

    public Optional<Patient> getByAuthId(String authId) {
        return patientRepository.findByAuthId(authId);
    }

    public Result deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new NotFoundException("Patient not found");
        }
        patientRepository.deleteById(id);
        return Result.DELETED;
    }

    // Search metoderna behöver inte ändras mycket mer än att ta bort DTO conversion
    public List<Patient> searchByFields(String pnr, String name) {
        if (pnr != null && !pnr.isBlank()) {
            return patientRepository.findByPersonalNumberContainingIgnoreCase(pnr);
        }
        if (name != null && !name.isBlank()) {
            return patientRepository.findByFullNameContainingIgnoreCase(name);
        }
        return getAllPatients();
    }
}
