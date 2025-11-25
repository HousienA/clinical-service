package com.fullstack.clinical_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "practitioners")
public class Practitioner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Koppling till Keycloak
    @Column(name = "auth_id", unique = true)
    private String authId;

    @Column(nullable = false)
    private String fullName;
    private String email;
    private String phone;

    public Practitioner() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuthId() { return authId; }
    public void setAuthId(String authId) { this.authId = authId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
