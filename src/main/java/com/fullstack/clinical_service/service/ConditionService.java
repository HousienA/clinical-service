package com.fullstack.clinical_service.service;

import com.fullstack.clinical_service.entity.Condition;
import com.fullstack.clinical_service.repository.ConditionRepository;
import com.fullstack.clinical_service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConditionService {

    private final ConditionRepository conditionRepository;

    public ConditionService(ConditionRepository conditionRepository) {
        this.conditionRepository = conditionRepository;
    }

    public List<Condition> getAllConditions() {
        return conditionRepository.findAll();
    }

    public Optional<Condition> getConditionById(Long id) {
        return conditionRepository.findById(id);
    }

    public List<Condition> getConditionsByPatientId(Long patientId) {
        return conditionRepository.findByPatientId(patientId);
    }

    public Condition createCondition(Condition condition) {
        // Här kan man validera att patienten finns om man vill
        return conditionRepository.save(condition);
    }

    public Condition updateCondition(Long id, Condition updated) {
        Condition existing = conditionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Condition not found"));

        existing.setConditionName(updated.getConditionName());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        // Diagnosdatum kanske inte ska ändras, men om det behövs:
        if (updated.getDiagnosedDate() != null) {
            existing.setDiagnosedDate(updated.getDiagnosedDate());
        }

        return conditionRepository.save(existing);
    }

    public void deleteCondition(Long id) {
        if (!conditionRepository.existsById(id)) {
            throw new NotFoundException("Condition not found");
        }
        conditionRepository.deleteById(id);
    }
}
