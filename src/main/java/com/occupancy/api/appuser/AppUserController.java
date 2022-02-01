package com.occupancy.api.appuser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(path = "appUser")
public class AppUserController {

    AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @GetMapping
    public Map<String, String> currentUse(AppUserService appUserService){
        return appUserService.getDetails();
    }

    @PutMapping(path = "{appUserId}")
    public void setToManager(
            @PathVariable("appUserId") Long appUserId,
            @RequestParam(required = true) Long organizationId){
        appUserService.setToManager(organizationId,appUserId);
    }
}
