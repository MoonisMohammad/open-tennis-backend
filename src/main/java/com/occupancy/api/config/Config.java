package com.occupancy.api.config;

import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRole;
import com.occupancy.api.appuser.AppUserService;
import com.occupancy.api.device.DeviceRepository;
import com.occupancy.api.facility.Facility;
import com.occupancy.api.facility.FacilityRepository;
import com.occupancy.api.organization.Organization;
import com.occupancy.api.organization.OrganizationRepository;
import com.occupancy.api.organization.OrganizationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {

    @Bean
    CommandLineRunner commandLineRunnerOrganiztion(OrganizationRepository organizationRepository){
        return args -> {
            Organization org1 = new Organization("TestOrganization" );
            organizationRepository.save(org1);
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerAppUser(AppUserService appUserService){
        return args -> {
            AppUser admin1 = new AppUser(
                    "Admin1",
                    "Occupancy2022",
                    AppUserRole.ADMIN
            );
            AppUser testManager = new AppUser(
                    "test",
                    "manager",
                    "manager@test.com",
                    "Occupancy2022",
                    AppUserRole.MANAGER
            );
            testManager.setToManager(Long.valueOf(1));
            AppUser testUser = new AppUser(
                    "test",
                    "user",
                    "User@test.com",
                    "Occupancy2022",
                    AppUserRole.USER
            );
            appUserService.signUpUser(admin1);
            appUserService.signUpUser(testManager);
            appUserService.signUpUser(testUser);
            appUserService.enableAppUser(admin1.getEmail());
            appUserService.enableAppUser(testManager.getEmail());
            appUserService.enableAppUser(testUser.getEmail());
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerFacility(FacilityRepository repository){
        return args -> {
            Facility f1 = new Facility(
                    Long.valueOf("1"),
                    "Carleton court",
                    "Carleton"
            );
            Facility f2 = new Facility(
                    Long.valueOf("2"),
                    "Ottawa court",
                    "Ottawa park"
            );
            Facility f3 = new Facility(
                    Long.valueOf("1"),
                    "Ottawa court",
                    "Ottawa park"
            );
            repository.saveAll(
                    List.of(f1,f2,f3)
            );
        };
    }

}
