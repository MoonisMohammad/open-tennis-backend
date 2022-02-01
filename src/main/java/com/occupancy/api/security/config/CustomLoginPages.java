package com.occupancy.api.security.config;

import com.occupancy.api.registration.RegistrationRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "status")
public class CustomLoginPages {

    @GetMapping(path = "/success")
    public Map<String, String> successfulLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("authentication", "true");
        map.put("message", "Login successful");
        return map;
    }

    @GetMapping(path = "/fail")
    public Map<String, String> failedLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("authentication", "false");
        map.put("message", "Login unsuccessful");
        return map;
    }


}
