package com.fullstack.clinical_service.repository;

import com.fullstack.clinical_service.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
    List<Condition> findByPatient_Id(Long patientId);
}
