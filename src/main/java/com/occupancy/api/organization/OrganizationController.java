package com.occupancy.api.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "organization")
public class OrganizationController {

    OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService){
        this.organizationService = organizationService;
    }

    //returns all organizations
    @GetMapping
    public  List<Organization> getOrganizations(){
        return organizationService.getOrganizations();
    }

    //return organization with given id
    @GetMapping(path = "{organizationId}")
    public  Organization getOrganizationWithId(@PathVariable("organizationId") Long organizationId){
        return organizationService.getOrganizationWithId(organizationId);
    }

    //adds a new organization
    @PostMapping
    public void addOrganization(@RequestBody Organization organization){
        organizationService.addNewOrganization(organization);
    }

    //deletes the organization with provided organization id
    @DeleteMapping(path = "{organizationId}")
    public void deleteOrganization(
            @PathVariable("organizationId") Long organizationId){
        organizationService.deleteOrganization(organizationId);
    }

    //updates the info of an organization
    @PutMapping(path = "{organizationId}")
    public void updateOrganization(@PathVariable("organizationId") Long organizationId,
                                   @RequestParam(required = true) String name){
        organizationService.updateOrganization(organizationId,name);
    }

}
