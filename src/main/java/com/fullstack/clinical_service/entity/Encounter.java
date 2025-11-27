package com.fullstack.clinical_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "encounters")
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties("encounters")
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime encounterDate;

    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Vi vill oftast inte ladda alla observationer direkt i encounter-listan, men om vi gör det, ignorera encounter-fältet i dem
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("encounter")
    private List<Observation> observations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "practitioner_id")
    private Practitioner practitioner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    // --- Transient fält ---
    @Transient private Long patientId;
    @Transient private Long practitionerId;
    @Transient private Long locationId;

    public Encounter() {}

    // Getters och Setters (Kopiera in dina getters/setters här som vanligt)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public LocalDateTime getEncounterDate() { return encounterDate; }
    public void setEncounterDate(LocalDateTime encounterDate) { this.encounterDate = encounterDate; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<Observation> getObservations() { return observations; }
    public void setObservations(List<Observation> observations) { this.observations = observations; }
    public Practitioner getPractitioner() { return practitioner; }
    public void setPractitioner(Practitioner practitioner) { this.practitioner = practitioner; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Long getPatientId() {
        // 1. Om input från frontend (transient fält satt)
        if (this.patientId != null) {
            return this.patientId;
        }
        // 2. Om output från databasen (hämta från relationen)
        if (this.patient != null) {
            return this.patient.getId();
        }
        return null;
    }

    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getPractitionerId() {
        if(this.practitionerId != null) {
            return this.practitionerId;
        }
        if(this.practitioner != null) {
            return this.practitioner.getId();
        }
        return null;
    }
    public void setPractitionerId(Long practitionerId) { this.practitionerId = practitionerId; }
    public Long getLocationId() {
        if(this.locationId != null) {
            return this.locationId;
        }
        if(this.location != null) {
            return this.location.getId();
        }
        return null;

    }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
}
