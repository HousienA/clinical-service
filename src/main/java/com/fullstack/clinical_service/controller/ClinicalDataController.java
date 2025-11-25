package com.fullstack.clinical_service.controller;

import com.fullstack.clinical_service.entity.*;
import com.fullstack.clinical_service.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinical")
public class ClinicalDataController {

    private final PatientService patientService;
    private final PractitionerService practitionerService;
    private final EncounterService encounterService;
    private final LocationService locationService;

    public ClinicalDataController(PatientService patientService,
                                  PractitionerService practitionerService,
                                  EncounterService encounterService,
                                  LocationService locationService) {
        this.patientService = patientService;
        this.practitionerService = practitionerService;
        this.encounterService = encounterService;
        this.locationService = locationService;
    }

    // --- PRACTITIONERS ---
    @GetMapping("/practitioners")
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitionerService.getAllPractitioners());
    }

    @PostMapping("/practitioners")
    public ResponseEntity<Practitioner> createPractitioner(@RequestBody Practitioner practitioner) {
        return ResponseEntity.ok(practitionerService.createPractitioner(practitioner));
    }

    // --- ENCOUNTERS ---
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

    // --- LOCATIONS ---
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations(@RequestParam(required = false) Long orgId,
                                                       @RequestParam(required = false) String q) {
        return ResponseEntity.ok(locationService.search(orgId, q));
    }


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
        patientService.createPatient(patient); // OBS: Din service returnerar void eller Result
        return ResponseEntity.ok("Patient created");
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        patientService.updatePatient(id, patient);
        return ResponseEntity.ok("Patient updated");
    }

}
