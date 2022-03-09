package com.occupancy.api.registration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    //take the new user details in th form of a registration request
    @PostMapping
    public Map<String, String> register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    //takes confirmation toke passes to service for validation
    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
