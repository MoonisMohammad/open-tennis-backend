package com.occupancy.api.data;

import com.occupancy.api.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "data/upload")
public class DataUploadController {

    private final DataService dataService;

    @Autowired
    public DataUploadController(DataService dataService){
        this.dataService = dataService;
    }

    @GetMapping
    public String test(){
        return "hello";
    }

    @PostMapping(path = "{authorizationId}")
    public void upload(@PathVariable("authorizationId")  String authorizationId,
                       @RequestBody Data data){
        dataService.upload(authorizationId,data);
    }
}
