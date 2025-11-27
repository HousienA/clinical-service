package com.fullstack.clinical_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observations")
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    @JsonIgnore // VIKTIGT: Skicka inte med hela Encounter-objektet i JSON-svaret
    private Encounter encounter;

    @Column(nullable = false)
    private String observationType;

    private String value;
    private String unit;
    private LocalDateTime observedAt;

    // --- Transient fält för input ---
    @Transient
    private Long encounterId;
    // --------------------------------

    public Observation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Encounter getEncounter() { return encounter; }
    public void setEncounter(Encounter encounter) { this.encounter = encounter; }

    public String getObservationType() { return observationType; }
    public void setObservationType(String observationType) { this.observationType = observationType; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getObservedAt() { return observedAt; }
    public void setObservedAt(LocalDateTime observedAt) { this.observedAt = observedAt; }

    public Long getEncounterId() {
        // 1. Om vi just har skickat in data (input), använd det transienta fältet
        if (this.encounterId != null) {
            return this.encounterId;
        }
        // 2. Om vi hämtar från databasen (output), hämta ID:t från relationen
        if (this.encounter != null) {
            return this.encounter.getId();
        }
        return null;
    }
    public void setEncounterId(Long encounterId) { this.encounterId = encounterId; }
}
