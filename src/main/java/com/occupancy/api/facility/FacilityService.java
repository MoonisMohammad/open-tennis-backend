package com.occupancy.api.facility;

import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRepository;
import com.occupancy.api.appuser.AppUserService;
import com.occupancy.api.facility.distance_calculator.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository,
                           AppUserRepository appUserRepository) {
        this.facilityRepository = facilityRepository;
        this.appUserRepository = appUserRepository;
    }

    //return all facilities in database
    public List<Facility> getFacilities(){
        return facilityRepository.findAll();
    }

    //return all facilities with provided id
    public Facility getFacilityWithId(Long facilityId) {
        Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);
        if(!facilityOptional.isPresent()) {
            throw new IllegalStateException(
                    "facility with id "+facilityId+"does not exists");
        }
        return facilityOptional.get();
    }

    //get facilities owned by the user
    public List<Facility> getOwnedFacilities(){
        AppUser appUser = getCurrentUser();
        return facilityRepository.findByOwnerId(appUser.getOrganizationId());
    }

    //uses filtering params to get in range facilities
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

    //add new facility to database
    public void addNewFacility(Facility facility){
        AppUser appUser = getCurrentUser();
        facility.setOwnerId(appUser.getOrganizationId());
        facilityRepository.save(facility);
    }

    //delete a facility with given id from database
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

    //update the  facility  with given id
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

    //gets list of facilities favourite by user
    public List<Facility> getFavouriteFacilities() {
        Long appUserId = getCurrentUser().getId();
        AppUser appUser = appUserRepository.findById(appUserId).get();
        ArrayList<Long> facilityIds = new ArrayList<Long>();
        ArrayList<Facility> facilities = new ArrayList<Facility>();
        for(int i = 0;i < appUser.getFavouritesSize();i++){

            facilities.add(getFacilityWithId(appUser.getFavourites()[i]));
        }
        return  facilities;
    }

    //ad ne facility to favourites
    @Transactional
    public void addFavourite(Long facilityId){
        Long appUserId = getCurrentUser().getId();
        AppUser appUser = appUserRepository.findById(appUserId).get();
        Facility facility = getFacilityWithId(facilityId);
        appUser.addFavourite(facilityId);
    }

    //remove a facility from favourites
    @Transactional
    public void removeFavourite(Long facilityId){
        Long appUserId = getCurrentUser().getId();
        AppUser appUser = appUserRepository.findById(appUserId).get();
        Facility facility = getFacilityWithId(facilityId);
        appUser.removeFavourites(facilityId);
    }

    //get a list all city supported by the backend
    public City[] getCities(){return City.values();}

    //get information of current user
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
