package com.fullstack.clinical_service.controller;

import com.fullstack.clinical_service.entity.*;
import com.fullstack.clinical_service.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clinical")
public class ClinicalDataController {

    private final PatientService patientService;
    private final PractitionerService practitionerService;
    private final EncounterService encounterService;
    private final LocationService locationService;

    public ClinicalDataController(
            PatientService patientService,
            PractitionerService practitionerService,
            EncounterService encounterService,
            LocationService locationService) {
        this.patientService = patientService;
        this.practitionerService = practitionerService;
        this.encounterService = encounterService;
        this.locationService = locationService;
    }

    // ==================== ONBOARDING ENDPOINTS ====================

    /**
     * Check if current user has a profile (Patient or Practitioner)
     * Called on first login to determine if onboarding is needed
     */
    @GetMapping("/profile/exists")
    public ResponseEntity<?> checkProfileExists(@AuthenticationPrincipal Jwt jwt) {
        String keycloakUserId = jwt.getSubject();  // Get Keycloak user ID from JWT

        // Check if user has a Patient profile
        Optional<Patient> patient = patientService.getByAuthId(keycloakUserId);
        if (patient.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "profileType", "PATIENT",
                    "profile", patient.get()
            ));
        }

        // Check if user has a Practitioner profile
        Optional<Practitioner> practitioner = practitionerService.getByAuthId(keycloakUserId);
        if (practitioner.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "profileType", "PRACTITIONER",
                    "profile", practitioner.get()
            ));
        }

        // No profile found - user needs onboarding
        return ResponseEntity.ok(Map.of(
                "exists", false,
                "keycloakUserId", keycloakUserId,
                "email", jwt.getClaimAsString("email"),
                "username", jwt.getClaimAsString("preferred_username")
        ));
    }

    /**
     * Complete patient onboarding
     */
    @PostMapping("/onboarding/patient")
    public ResponseEntity<?> completePatientOnboarding(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Patient patientData) {

        String keycloakUserId = jwt.getSubject();

        // Check if profile already exists
        if (patientService.getByAuthId(keycloakUserId).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Profile already exists"));
        }

        // Set Keycloak user ID
        patientData.setAuthId(keycloakUserId);

        // Validate required fields
        if (patientData.getFullName() == null || patientData.getFullName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Full name is required"));
        }
        if (patientData.getPersonalNumber() == null || patientData.getPersonalNumber().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Personal number is required"));
        }

        // Create patient profile
        patientService.createPatient(patientData);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Patient profile created successfully",
                "profile", patientData
        ));
    }

    /**
     * Complete practitioner onboarding
     */
    @PostMapping("/onboarding/practitioner")
    public ResponseEntity<?> completePractitionerOnboarding(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Practitioner practitionerData) {

        String keycloakUserId = jwt.getSubject();

        // Check if profile already exists
        if (practitionerService.getByAuthId(keycloakUserId).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Profile already exists"));
        }

        // Set Keycloak user ID
        practitionerData.setAuthId(keycloakUserId);

        // Validate required fields
        if (practitionerData.getFullName() == null || practitionerData.getFullName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Full name is required"));
        }

        // Create practitioner profile
        practitionerService.createPractitioner(practitionerData);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Practitioner profile created successfully",
                "profile", practitionerData
        ));
    }

    // ==================== PRACTITIONERS ====================

    @GetMapping("/practitioners")
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitionerService.getAllPractitioners());
    }

    @PostMapping("/practitioners")
    public ResponseEntity<Practitioner> createPractitioner(@RequestBody Practitioner practitioner) {
        return ResponseEntity.ok(practitionerService.createPractitioner(practitioner));
    }

    // ==================== ENCOUNTERS ====================

    @GetMapping("/encounters")
    public ResponseEntity<List<Encounter>> getAllEncounters() {
        return ResponseEntity.ok(encounterService.getAllEncounters());
    }

    @GetMapping("/encounters/patient/{id}")
    public ResponseEntity<List<Encounter>> getEncountersByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(encounterService.getEncountersByPatientId(id));
    }

    @PostMapping("/encounters")
    public ResponseEntity<Encounter> createEncounter(@RequestBody Encounter encounter) {
        return ResponseEntity.ok(encounterService.createEncounter(encounter));
    }

    // ==================== LOCATIONS ====================

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations(
            @RequestParam(required = false) Long orgId,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(locationService.search(orgId, q));
    }

    // ==================== PATIENTS ====================

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/patients")
    public ResponseEntity<String> createPatient(@RequestBody Patient patient) {
        patientService.createPatient(patient);
        return ResponseEntity.ok("Patient created");
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        patientService.updatePatient(id, patient);
        return ResponseEntity.ok("Patient updated");
    }
}
