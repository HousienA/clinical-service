package com.fullstack.clinical_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // VIKTIGT
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(name = "personal_number", nullable = false, unique = true)
    private String personalNumber;

    private String email;
    private String phone;

    // NYTT: Koppling till Keycloak istället för User-tabell
    @Column(name = "auth_id", unique = true)
    private String authId;

    @JsonIgnore // Förhindrar oändlig loop
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Encounter> encounters = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Condition> conditions = new ArrayList<>();

    // Messages finns i en annan tjänst nu, så vi tar bort relationen helt härifrån
    // Om du vill ha kvar den måste den vara @Transient, men bäst att ta bort.

    @ManyToOne(fetch = FetchType.EAGER) // Eager kan vara bra om vi skippar DTO
    @JoinColumn(name = "primary_practitioner_id")
    private Practitioner primaryPractitioner;

    public Patient() {}

    // Getters och Setters... glöm inte authId!
    public String getAuthId() { return authId; }
    public void setAuthId(String authId) { this.authId = authId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPersonalNumber() { return personalNumber; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<Encounter> getEncounters() { return encounters; }
    public void setEncounters(List<Encounter> encounters) { this.encounters = encounters; }
    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
    public Practitioner getPrimaryPractitioner() { return primaryPractitioner; }
    public void setPrimaryPractitioner(Practitioner primaryPractitioner) { this.primaryPractitioner = primaryPractitioner; }
}
