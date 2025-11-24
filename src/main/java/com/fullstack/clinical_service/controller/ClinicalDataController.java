package com.fullstack.clinical_service.controller;

import com.fullstack.clinical_service.entity.*;
import com.fullstack.clinical_service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ClinicalDataController {

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private LocationService locationService;

    // ==================== ENCOUNTER ENDPOINTS ====================

    @GetMapping("/encounters")
    public ResponseEntity<List<Encounter>> getAllEncounters() {
        return ResponseEntity.ok(encounterService.getAllEncounters());
    }

    @GetMapping("/encounters/{id}")
    public ResponseEntity<Encounter> getEncounterById(@PathVariable Long id) {
        return encounterService.getEncounterById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/encounters/patient/{patientId}")
    public ResponseEntity<List<Encounter>> getEncountersByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(encounterService.getEncountersByPatient(patientId));
    }

    @GetMapping("/encounters/practitioner/{practitionerId}")
    public ResponseEntity<List<Encounter>> getEncountersByPractitioner(@PathVariable Long practitionerId) {
        return ResponseEntity.ok(encounterService.getEncountersByPractitioner(practitionerId));
    }

    @GetMapping("/encounters/practitioner/{practitionerId}/date/{date}")
    public ResponseEntity<List<Encounter>> getEncountersByPractitionerAndDate(
            @PathVariable Long practitionerId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(encounterService.getEncountersByPractitionerAndDate(practitionerId, date));
    }

    @PostMapping("/encounters/patient/{patientId}")
    public ResponseEntity<String> createEncounter(
            @PathVariable Long patientId,
            @RequestBody Encounter encounter
    ) {
        encounterService.createEncounter(patientId, encounter);
        return ResponseEntity.ok("Encounter created successfully");
    }

    @PutMapping("/encounters/{id}")
    public ResponseEntity<String> updateEncounter(
            @PathVariable Long id,
            @RequestBody Encounter encounter
    ) {
        encounterService.updateEncounter(id, encounter);
        return ResponseEntity.ok("Encounter updated successfully");
    }

    @DeleteMapping("/encounters/{id}")
    public ResponseEntity<String> deleteEncounter(@PathVariable Long id) {
        encounterService.deleteEncounter(id);
        return ResponseEntity.ok("Encounter deleted successfully");
    }

    // ==================== OBSERVATION ENDPOINTS ====================

    @GetMapping("/observations")
    public ResponseEntity<List<Observation>> getAllObservations() {
        return ResponseEntity.ok(observationService.getAllObservations());
    }

    @GetMapping("/observations/{id}")
    public ResponseEntity<Observation> getObservationById(@PathVariable Long id) {
        return observationService.getObservationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/observations/patient/{patientId}")
    public ResponseEntity<List<Observation>> getObservationsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(observationService.getObservationsByPatient(patientId));
    }

    @GetMapping("/observations/encounter/{encounterId}")
    public ResponseEntity<List<Observation>> getObservationsByEncounter(@PathVariable Long encounterId) {
        return ResponseEntity.ok(observationService.getObservationsByEncounter(encounterId));
    }

    @PostMapping("/observations")
    public ResponseEntity<String> createObservation(@RequestBody Observation observation) {
        observationService.createObservation(observation);
        return ResponseEntity.ok("Observation created successfully");
    }

    @PutMapping("/observations/{id}")
    public ResponseEntity<String> updateObservation(
            @PathVariable Long id,
            @RequestBody Observation observation
    ) {
        observationService.updateObservation(id, observation);
        return ResponseEntity.ok("Observation updated successfully");
    }

    @DeleteMapping("/observations/{id}")
    public ResponseEntity<String> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.ok("Observation deleted successfully");
    }

    // ==================== PRACTITIONER ENDPOINTS ====================

    @GetMapping("/practitioners")
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitionerService.getAllPractitioners());
    }

    @GetMapping("/practitioners/{id}")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable Long id) {
        return practitionerService.getPractitionerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/practitioners/search")
    public ResponseEntity<List<Practitioner>> searchPractitioners(
            @RequestParam(value = "name", required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(practitionerService.searchByName(name));
        }
        return ResponseEntity.ok(practitionerService.getAllPractitioners());
    }

    @PostMapping("/practitioners")
    public ResponseEntity<String> createPractitioner(@RequestBody Practitioner practitioner) {
        practitionerService.createPractitioner(practitioner);
        return ResponseEntity.ok("Practitioner created successfully");
    }

    @PutMapping("/practitioners/{id}")
    public ResponseEntity<String> updatePractitioner(
            @PathVariable Long id,
            @RequestBody Practitioner practitioner
    ) {
        practitionerService.updatePractitioner(id, practitioner);
        return ResponseEntity.ok("Practitioner updated successfully");
    }

    @DeleteMapping("/practitioners/{id}")
    public ResponseEntity<String> deletePractitioner(@PathVariable Long id) {
        practitionerService.deletePractitioner(id);
        return ResponseEntity.ok("Practitioner deleted successfully");
    }

    // ==================== ORGANIZATION ENDPOINTS ====================

    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/organizations/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/organizations")
    public ResponseEntity<String> createOrganization(@RequestBody Organization organization) {
        organizationService.createOrganization(organization);
        return ResponseEntity.ok("Organization created successfully");
    }

    @PutMapping("/organizations/{id}")
    public ResponseEntity<String> updateOrganization(
            @PathVariable Long id,
            @RequestBody Organization organization
    ) {
        organizationService.updateOrganization(id, organization);
        return ResponseEntity.ok("Organization updated successfully");
    }

    @DeleteMapping("/organizations/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok("Organization deleted successfully");
    }

    // ==================== LOCATION ENDPOINTS ====================

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/locations")
    public ResponseEntity<String> createLocation(@RequestBody Location location) {
        locationService.createLocation(location);
        return ResponseEntity.ok("Location created successfully");
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<String> updateLocation(
            @PathVariable Long id,
            @RequestBody Location location
    ) {
        locationService.updateLocation(id, location);
        return ResponseEntity.ok("Location updated successfully");
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Location deleted successfully");
    }
}
