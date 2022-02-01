package com.occupancy.api.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@RestController
@RequestMapping(path = "organization")
public class OrganizationController {

    OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService){
        this.organizationService = organizationService;
    }

    @GetMapping
    public  List<Organization> getOrganizations(){
        return organizationService.getOrganizations();
    }

    @GetMapping(path = "{organizationId}")
    public  Organization getOrganizationWithId(@PathVariable("organizationId") Long organizationId){
        return organizationService.getOrganizatioWithId(organizationId);
    }

    @PostMapping
    public void addOrganization(@RequestBody Organization organization){
        organizationService.addNewOrganization(organization);
    }

    @DeleteMapping(path = "{organizationId}")
    public void deleteOrganization(
            @PathVariable("organizationId") Long organizationId){
        organizationService.deleteOrganization(organizationId);
    }

    @PutMapping(path = "{organizationId}")
    public void updateOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestParam(required = true) String name){
        organizationService.updateOrganization(organizationId,name);
    }

}
