package com.occupancy.api.facility;

import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.distance_calculator.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<Facility> getFacilities(){
        return facilityRepository.findAll();
    }

    public Facility getFacilityWithId(Long facilityId) {
        Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);
        if(!facilityOptional.isPresent()) {
            throw new IllegalStateException(
                    "facility with id "+facilityId+"does not exists");
        }
        return facilityOptional.get();
    }

    public List<Facility> getOwnedFacilities(){
        AppUser appUser = getCurrentUser();
        return facilityRepository.findByOwnerId(appUser.getOrganizationId());
    }

    public List<Facility> getFilteredFacilities(Double latitude,
                                                Double longitude,
                                                String city,
                                                Double range,
                                                char unit) {

        System.out.println(city);

        List<Facility> facilities = facilityRepository.findByCity(city);
        System.out.println(facilities.size());
        if(facilities.isEmpty()) {
            throw new IllegalStateException(
                    "No facilities in city "+city);
        }
        ArrayList<Facility> filtered = new ArrayList<Facility>();
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        for(Facility facility : facilities){
            if(distanceCalculator.inRange(facility,latitude,longitude,unit,range)){
                filtered.add(facility);
            }
        }
        return filtered;
    }

    public void addNewFacility(Facility facility){
        AppUser appUser = getCurrentUser();
        facility.setOwnerId(appUser.getOrganizationId());
        facilityRepository.save(facility);
    }

    public void deleteFacility(Long facilityId){
        AppUser appUser = getCurrentUser();
        Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);
        if(!facilityOptional.isPresent()) {
            throw new IllegalStateException(
                    "facility with id "+facilityId+"does not exist");
        }
        Long facilityOwnerId = facilityOptional.get().getOwnerId();
        if(appUser.getOrganizationId().equals(facilityOwnerId)) {
            facilityRepository.deleteById(facilityId);
        }else{
            throw new IllegalStateException(
                    "facility with id "+facilityId+"not owned by user");
        }
    }

    @Transactional
    public void updateFacility(Long facilityId,
                               String name,
                               String city,
                               Double latitude,
                               Double longitude) {
        AppUser appUser = getCurrentUser();
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() ->
                        new IllegalStateException("facility with id " + facilityId + "does not exist"));
        Long facilityOwnerId = facility.getOwnerId();
        if(!appUser.getOrganizationId().equals(facilityOwnerId)){
            throw new IllegalStateException(
                    "facility with id "+facilityId+"not owned by user");
        }if(name != null && name.length()>0 && !Objects.equals(facility.getName(),name)) {
            facility.setName(name);
        }if(city !=null && city.length()>0&&!Objects.equals(facility.getCity(),city)) {
            facility.setCity(city);
        }if(latitude !=null && !Objects.equals(facility.getLatitude(),latitude)) {
            facility.setLatitude(latitude);
        }if(longitude !=null && !Objects.equals(facility.getLongitude(),longitude)) {
            facility.setLongitude(longitude);
        }
    }

    public City[] getCities(){return City.values();}

    public AppUser getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser;
        if (principal instanceof AppUser) {
            appUser = ((AppUser)principal);
            return appUser;
        }else {
            return null;
        }
    }

}
