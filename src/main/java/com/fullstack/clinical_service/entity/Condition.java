package com.fullstack.clinical_service.entity;

import com.fullstack.clinical_service.enums.ConditionStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String conditionName;

    private String description;

    private LocalDate diagnosedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionStatus status;

    public Condition() {
        this.status = ConditionStatus.ACTIVE;
    }

    public Condition(Patient patient, String conditionName) {
        this.patient = patient;
        this.conditionName = conditionName;
        this.diagnosedDate = LocalDate.now();
        this.status = ConditionStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getConditionName() { return conditionName; }
    public void setConditionName(String conditionName) { this.conditionName = conditionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDiagnosedDate() { return diagnosedDate; }
    public void setDiagnosedDate(LocalDate diagnosedDate) { this.diagnosedDate = diagnosedDate; }

    public ConditionStatus getStatus() { return status; }
    public void setStatus(ConditionStatus status) { this.status = status; }
}

