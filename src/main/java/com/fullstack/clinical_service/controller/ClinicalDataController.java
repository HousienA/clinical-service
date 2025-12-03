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
    private final ConditionService conditionService;
    private final ObservationService observationService;
    private final OrganizationService organizationService;

    public ClinicalDataController(
            PatientService patientService,
            PractitionerService practitionerService,
            EncounterService encounterService,
            LocationService locationService,
            ConditionService conditionService,
            ObservationService observationService,
            OrganizationService organizationService) {
        this.patientService = patientService;
        this.practitionerService = practitionerService;
        this.encounterService = encounterService;
        this.organizationService = organizationService;
        this.locationService = locationService;
        this.conditionService = conditionService;
        this.observationService = observationService;
    }

    // ==================== ONBOARDING ENDPOINTS ====================

    /**
     * Check if current user has a profile (Patient or Practitioner)
     * Called on first login to determine if onboarding is needed
     */
    @GetMapping("/profile/exists")
    public ResponseEntity<?> checkProfileExists(@AuthenticationPrincipal Jwt jwt) {
        String keycloakUserId = jwt.getSubject(); // Get Keycloak user ID from JWT

        // Check if user has a Patient profile
        Optional<Patient> patient = patientService.getByAuthId(keycloakUserId);
        if (patient.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "profileType", "PATIENT",
                    "profile", patient.get()));
        }

        // Check if user has a Practitioner profile
        Optional<Practitioner> practitioner = practitionerService.getByAuthId(keycloakUserId);
        if (practitioner.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "profileType", "PRACTITIONER",
                    "profile", practitioner.get()));
        }

        // No profile found - user needs onboarding
        return ResponseEntity.ok(Map.of(
                "exists", false,
                "keycloakUserId", keycloakUserId,
                "email", jwt.getClaimAsString("email"),
                "username", jwt.getClaimAsString("preferred_username")));
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
                "profile", patientData));
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
                "profile", practitionerData));
    }

    // ==================== PRACTITIONERS ====================

    @GetMapping("/practitioners")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('STAFF')")
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitionerService.getAllPractitioners());
    }

    @GetMapping("/practitioners/{id}/patients")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<Patient>> getPatientsByPractitioner(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientsByPractitionerId(id));
    }

    @GetMapping("/practitioners/{id}/encounters")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<Encounter>> getEncountersByPractitioner(
            @PathVariable Long id,
            @RequestParam(required = false) String date) {

        if (date != null) {
            return ResponseEntity
                    .ok(encounterService.getEncountersByPractitionerIdAndDate(id, java.time.LocalDate.parse(date)));
        }
        return ResponseEntity.ok(encounterService.getEncountersByPractitionerId(id));
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

    @GetMapping("/encounters/{id}")
    public ResponseEntity<Encounter> getEncounterById(@PathVariable Long id) {
        return encounterService.getEncounterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/encounters/patient/{id}")
    public ResponseEntity<List<Encounter>> getEncountersByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(encounterService.getEncountersByPatientId(id));
    }

    @PostMapping("/encounters")
    public ResponseEntity<Encounter> createEncounter(@RequestBody Encounter encounter) {
        return ResponseEntity.ok(encounterService.createEncounter(encounter));
    }

    // ==================== ORGANIZATIONS ====================
    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> getOrganizations(@RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(organizationService.search(q));
        }
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/organizations/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    // ==================== LOCATIONS ====================
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations(
            @RequestParam(required = false, name = "organizationId") Long organizationId,
            @RequestParam(required = false) String q) {

        // Debug-utskrift (valfritt, men bra för att se om backend tar emot id:t)
        System.out.println("Fetching locations for organizationId: " + organizationId);

        return ResponseEntity.ok(locationService.search(organizationId, q));
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id)); // Observera: Service kastar exception om ej
                                                                       // hittad, vilket hanteras globalt
    }
    // ==================== PATIENTS ====================

    @GetMapping("/patients")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('STAFF') or hasRole('ADMIN')")
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
    public ResponseEntity<Map<String, String>> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        patientService.updatePatient(id, patient);
        return ResponseEntity.ok(Map.of("message", "Patient updated"));
    }

    // ==================== CONDITIONS ====================
    @GetMapping("/conditions")
    public ResponseEntity<List<Condition>> getAllConditions() {
        return ResponseEntity.ok(conditionService.getAllConditions());
    }

    @GetMapping("/conditions/{id}")
    public ResponseEntity<Condition> getConditionById(@PathVariable Long id) {
        return conditionService.getConditionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/conditions/patient/{patientId}")
    public ResponseEntity<List<Condition>> getConditionsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(conditionService.getConditionsByPatientId(patientId));
    }

    @PostMapping("/conditions")
    public ResponseEntity<Condition> createCondition(@RequestBody Condition condition) {
        return ResponseEntity.ok(conditionService.createCondition(condition));
    }

    @PutMapping("/conditions/{id}")
    public ResponseEntity<Condition> updateCondition(@PathVariable Long id, @RequestBody Condition condition) {
        return ResponseEntity.ok(conditionService.updateCondition(id, condition));
    }

    @DeleteMapping("/conditions/{id}")
    public ResponseEntity<Void> deleteCondition(@PathVariable Long id) {
        conditionService.deleteCondition(id);
        return ResponseEntity.ok().build();
    }

    // ==================== OBSERVATIONS ====================
    @GetMapping("/observations")
    public ResponseEntity<List<Observation>> getAllObservations() {
        return ResponseEntity.ok(observationService.getAllObservations());
    }

    @GetMapping("/observations/{id}")
    public ResponseEntity<Observation> getObservationById(@PathVariable Long id) {
        return observationService.getObservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/observations/encounter/{encounterId}")
    public ResponseEntity<List<Observation>> getObservationsByEncounter(@PathVariable Long encounterId) {
        return ResponseEntity.ok(observationService.getObservationsByEncounterId(encounterId));
    }

    @PostMapping("/observations")
    public ResponseEntity<Observation> createObservation(@RequestBody Observation observation) {
        return ResponseEntity.ok(observationService.createObservation(observation));
    }

    @PutMapping("/observations/{id}")
    public ResponseEntity<Observation> updateObservation(@PathVariable Long id, @RequestBody Observation observation) {
        return ResponseEntity.ok(observationService.updateObservation(id, observation));
    }

    @DeleteMapping("/observations/{id}")
    public ResponseEntity<Void> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.ok().build();
    }
}
