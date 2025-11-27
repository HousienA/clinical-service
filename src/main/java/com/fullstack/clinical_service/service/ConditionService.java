package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Condition;
import com.fullstack.clinical_service.entity.Patient;
import com.fullstack.clinical_service.repository.ConditionRepository;
import com.fullstack.clinical_service.repository.PatientRepository;
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConditionService {

    private final ConditionRepository conditionRepository;
    private final PatientRepository patientRepository; // NYTT

    public ConditionService(ConditionRepository conditionRepository, PatientRepository patientRepository) {
        this.conditionRepository = conditionRepository;
        this.patientRepository = patientRepository;
    }

    public List<Condition> getAllConditions() {
        return conditionRepository.findAll();
    }

    public Optional<Condition> getConditionById(Long id) {
        return conditionRepository.findById(id);
    }

    public List<Condition> getConditionsByPatientId(Long patientId) {
        return conditionRepository.findByPatient_Id(patientId);
    }

    public Condition createCondition(Condition condition) {
        // Koppla patient via ID om det finns
        if (condition.getPatient() == null && condition.getPatientId() != null) {
            Patient p = patientRepository.findById(condition.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + condition.getPatientId()));
            condition.setPatient(p);
        } else if (condition.getPatient() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }

        return conditionRepository.save(condition);
    }

    public Condition updateCondition(Long id, Condition updated) {
        Condition existing = conditionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Condition not found"));

        existing.setConditionName(updated.getConditionName());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        if (updated.getDiagnosedDate() != null) {
            existing.setDiagnosedDate(updated.getDiagnosedDate());
        }

        return conditionRepository.save(existing);
    }

    public void deleteCondition(Long id) {
        conditionRepository.deleteById(id);
    }
}
