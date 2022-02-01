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

    @GetMapping(path = "{facilityId}")
    public Facility getFacilityWithId(
            @PathVariable("facilityId") Long facilityId){
        return facilityService.getFacilityWithId(facilityId);
    }

    @GetMapping
    public List<Facility> getFacilities(){
        return facilityService.getFacilities();
    }

    @GetMapping(path = "owned")
    public List<Facility> getOwnedFacilities(){
        return facilityService.getOwnedFacilities();
    }

    @PostMapping
    public void addNewFacility(
            @RequestBody Facility facility){
        facilityService.addNewFacility(facility);
    }

    @DeleteMapping(path = "{facilityId}")
    public void deleteFacility(
            @PathVariable("facilityId") Long facilityId){
        facilityService.deleteFacility(facilityId);
    }

    @PutMapping(path = "{facilityId}")
    public void updateFacility(
            @PathVariable("facilityId") Long facilityId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude){
        facilityService.updateFacility(facilityId,name,city,latitude,longitude);
    }



}
