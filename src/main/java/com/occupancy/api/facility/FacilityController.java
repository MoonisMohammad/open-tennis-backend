package com.occupancy.api.facility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "facility")
public class FacilityController {

    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    //returns facility with given id
    @GetMapping(path = "{facilityId}")
    public Facility getFacilityWithId(@PathVariable("facilityId") Long facilityId){
        return facilityService.getFacilityWithId(facilityId);
    }

    //returns all facilities
    @GetMapping
    public List<Facility> getFacilities(){
        return facilityService.getFacilities();
    }

    //returns a list of all cities supported by backend
    @GetMapping(path = "cities")
    public City[] getCities(){
        return facilityService.getCities();
    }

    //returns the list of cureent users favourite facilities
    @GetMapping(path = "favourite")
    public List<Facility> getFavouriteFacilities(){return facilityService.getFavouriteFacilities();}

    //adds new facility to favourite
    @PostMapping(path = "favourite/add/{facilityId}")
    public void addFavouriteFacilities(@PathVariable("facilityId") Long facilityId){
        facilityService.addFavourite(facilityId);
    }

    //removes facility with given id from favourite
    @DeleteMapping(path = "favourite/remove/{facilityId}")
    public void removeFavouriteFacilities(@PathVariable("facilityId") Long facilityId){
        facilityService.removeFavourite(facilityId);
    }

    //returns list of facilities owned by user organization
    @GetMapping(path = "owned")
    public List<Facility> getOwnedFacilities(){ return facilityService.getOwnedFacilities();}

    //return filtered facilities based on range, city and current latitude and longitude
    @GetMapping(path = "filters")
    public List<Facility> getFilteredFacilities(@RequestParam Double latitude,
                                                @RequestParam Double longitude,
                                                @RequestParam String city,
                                                @RequestParam Double range,
                                                @RequestParam char unit){
        return facilityService.getFilteredFacilities(latitude,longitude,city,range,unit);
    }

    //add new facility to the database
    @PostMapping
    public void addNewFacility(
            @RequestBody Facility facility){
        facilityService.addNewFacility(facility);
    }

    @DeleteMapping(path = "{facilityId}")
    public void deleteFacility(@PathVariable("facilityId") Long facilityId){
        facilityService.deleteFacility(facilityId);
    }

    @PutMapping(path = "{facilityId}")
    public void updateFacility(@PathVariable("facilityId") Long facilityId,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) Double latitude,
                               @RequestParam(required = false) Double longitude){
        facilityService.updateFacility(facilityId,
                name,city,latitude,longitude);
    }

}
