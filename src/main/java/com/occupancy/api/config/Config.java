package com.occupancy.api.config;

import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRole;
import com.occupancy.api.appuser.AppUserService;
import com.occupancy.api.device.Device;
import com.occupancy.api.device.DeviceRepository;
import com.occupancy.api.device.DeviceType;
import com.occupancy.api.facility.Facility;
import com.occupancy.api.facility.FacilityRepository;
import com.occupancy.api.organization.Organization;
import com.occupancy.api.organization.OrganizationRepository;
import com.occupancy.api.occupancyData.OccupancyDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    CommandLineRunner commandLineRunnerOrganization(OrganizationRepository organizationRepository){
        return args -> {
            Organization org1 = new Organization("Municipality Of Ottawa" );
            organizationRepository.save(org1);
            Organization org2 = new Organization("Lyndwood Tennis Club" );
            organizationRepository.save(org2);
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
            AppUser testManager1 = new AppUser(
                    "ottawa",
                    "manager",
                    "ottawaManager@test.com",
                    "Occupancy2022",
                    AppUserRole.MANAGER
            );
            testManager1.setToManager(Long.valueOf(1));
            AppUser testManager2 = new AppUser(
                    "lyndwood",
                    "manager",
                    "lyndwoodManager@test.com",
                    "Occupancy2022",
                    AppUserRole.MANAGER
            );
            testManager2.setToManager(Long.valueOf(2));
            AppUser testUser = new AppUser(
                    "test",
                    "user",
                    "user@test.com",
                    "Occupancy2022",
                    AppUserRole.USER
            );
            appUserService.signUpUser(admin1);
            appUserService.signUpUser(testManager1);
            appUserService.signUpUser(testManager2);
            appUserService.signUpUser(testUser);
            appUserService.enableAppUser(admin1.getEmail());
            appUserService.enableAppUser(testManager1.getEmail());
            appUserService.enableAppUser(testManager2.getEmail());
            appUserService.enableAppUser(testUser.getEmail());
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerFacility(FacilityRepository repository){
        return args -> {
            Facility facility = new Facility(
                    Long.valueOf(2),
                    "Lyndwood Tennis Club",
                    "MISSISSAUGA",
                    43.576630,
                    -79.571030
                    );
            repository.save(facility);
            ReadSampleData readSampleData = new ReadSampleData();
            repository.saveAll(readSampleData.readFacilities(Long.valueOf(1)));
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerDevice(DeviceRepository deviceRepository) {
        Device device1 = new Device("1OCC9876543210");
        Device device2 = new Device("2OCC9876543210");
        Device device3 = new Device("3OCC9876543210");
        return args -> {
            device1.register(
                    Long.valueOf(2),
                    Long.valueOf(1),
                    "Sample Data Cam",
                    3,
                    DeviceType.Tennis);
            device2.register(
                    Long.valueOf(2),
                    Long.valueOf(1),
                    "Test Upload Cam",
                    1,
                    DeviceType.Tennis);
            device3.register(
                    Long.valueOf(2),
                    Long.valueOf(1),
                    "Random created data Cam",
                    3,
                    DeviceType.Tennis);
            deviceRepository.save(device1);
            deviceRepository.save(device2);
            deviceRepository.save(device3);
        };
    }
    @Bean
    CommandLineRunner commandLineRunnerData(OccupancyDataRepository occupancyDataRepository){
        return args -> {
            ReadSampleData readSampleData = new ReadSampleData();
            occupancyDataRepository.saveAll(readSampleData.getOccupancyData(Long.valueOf(1),Long.valueOf(1)));
            occupancyDataRepository.saveAll(readSampleData.getRandomOccupancyData(Long.valueOf(3),Long.valueOf(1)));
        };
    }

}
