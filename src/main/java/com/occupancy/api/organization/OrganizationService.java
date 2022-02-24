package com.occupancy.api.organization;
import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public  OrganizationService(OrganizationRepository organizationRepository){
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getOrganizations() {
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            return organizationRepository.findAll();
        }else{
            throw new IllegalStateException(
                    "Only admin can do this action");
        }
    }

    public Organization getOrganizationWithId(Long organizationId){
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
            if (!organizationOptional.isPresent()) {
                throw new IllegalStateException(
                        "organization with id " + organizationId + "does not exists");
            }
            return organizationOptional.get();
        }else{
            throw new IllegalStateException(
                    "Only admin can do this action");
        }
    }

    public void addNewOrganization(Organization organization) {
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            organizationRepository.save(organization);
        }else{
        throw new IllegalStateException(
                "Only admin can do this action");
        }
    }

    public void deleteOrganization(Long organizationId){
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            organizationRepository.deleteById(organizationId);
        }else {
            throw new IllegalStateException(
                    "Only admin can do this action");
        }
    }

    @Transactional
    public void updateOrganization(Long organizationId,
                                   String name) {
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalStateException(
                            "organization with id " + organizationId + "does not exist"));
            organization.setName(name);
        }else {
            throw new IllegalStateException(
                    "Only admin can do this action");
        }
    }

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
