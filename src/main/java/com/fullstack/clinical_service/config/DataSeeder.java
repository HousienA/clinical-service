package com.fullstack.clinical_service.config;

import com.fullstack.clinical_service.entity.Location;
import com.fullstack.clinical_service.entity.Organization;
import com.fullstack.clinical_service.repository.LocationRepository;
import com.fullstack.clinical_service.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final OrganizationRepository organizationRepository;
    private final LocationRepository locationRepository;

    public DataSeeder(OrganizationRepository organizationRepository,
                      LocationRepository locationRepository) {
        this.organizationRepository = organizationRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        if (organizationRepository.count() > 0) {
            System.out.println("✓ Organizations already exist, skipping seed...");
            return;
        }

        System.out.println("Seeding Organizations and Locations...");


        Organization karolinska = new Organization("Karolinska Universitetssjukhuset");
        karolinska = organizationRepository.save(karolinska);

        createLocation("Akutmottagning", karolinska);
        createLocation("Medicinkliniken", karolinska);
        createLocation("Kirurgkliniken", karolinska);
        createLocation("Röntgenavdelning", karolinska);
        createLocation("Ortopedkliniken", karolinska);


        Organization vardcentral = new Organization("Centrala Vårdcentralen");
        vardcentral = organizationRepository.save(vardcentral);

        createLocation("Reception", vardcentral);
        createLocation("Läkarmottagning 1", vardcentral);
        createLocation("Läkarmottagning 2", vardcentral);
        createLocation("Läkarmottagning 3", vardcentral);
        createLocation("Provtagning", vardcentral);
        createLocation("BVC (Barnavårdscentral)", vardcentral);


        Organization sodersjukhuset = new Organization("Södersjukhuset");
        sodersjukhuset = organizationRepository.save(sodersjukhuset);

        createLocation("Akuten", sodersjukhuset);
        createLocation("Barnkliniken", sodersjukhuset);
        createLocation("Förlossningsavdelning", sodersjukhuset);
        createLocation("Ortopedkliniken", sodersjukhuset);
        createLocation("Röntgen", sodersjukhuset);


        Organization danderyd = new Organization("Danderyds sjukhus");
        danderyd = organizationRepository.save(danderyd);

        createLocation("Akutmottagning", danderyd);
        createLocation("Medicinkliniken", danderyd);
        createLocation("Kirurgkliniken", danderyd);
        createLocation("Kardiologkliniken", danderyd);

        System.out.println("Successfully seeded " + organizationRepository.count()
                + " organizations and " + locationRepository.count() + " locations!");
    }

    private void createLocation(String name, Organization organization) {
        Location location = new Location();
        location.setName(name);
        location.setOrganization(organization);
        locationRepository.save(location);
    }
}
