package com.fullstack.clinical_service.service;


import com.fullstack.clinical_service.entity.Practitioner;
import com.fullstack.clinical_service.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;

    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }

    public Optional<Practitioner> getPractitionerById(Long id) {
        return practitionerRepository.findById(id);
    }

    public Practitioner createPractitioner(Practitioner practitioner) {
        return practitionerRepository.save(practitioner);
    }

    public Practitioner updatePractitioner(Long id, Practitioner updatedPractitioner) {
        Practitioner practitioner = practitionerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practitioner not found"));

        practitioner.setFullName(updatedPractitioner.getFullName());
        practitioner.setEmail(updatedPractitioner.getEmail());
        practitioner.setPhone(updatedPractitioner.getPhone());
        practitioner.setLicenseNumber(updatedPractitioner.getLicenseNumber());
        practitioner.setSpecialization(updatedPractitioner.getSpecialization());

        return practitionerRepository.save(practitioner);
    }

    public void deletePractitioner(Long id) {
        if (!practitionerRepository.existsById(id)) {
            throw new RuntimeException("Practitioner not found");
        }
        practitionerRepository.deleteById(id);
    }

    public List<Practitioner> searchByName(String name) {
        return practitionerRepository.findByFullNameContainingIgnoreCase(name);
    }
}