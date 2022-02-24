package com.occupancy.api.occupancyData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "data/upload")
public class OccupancyDataUploadController {

    private final OccupancyDataService dataService;

    @Autowired
    public OccupancyDataUploadController(OccupancyDataService dataService){
        this.dataService = dataService;
    }

    @GetMapping
    public String test(){
        return "hello";
    }

    @PostMapping(path = "{authorizationId}")
    public void upload(@PathVariable("authorizationId")  String authorizationId,
                       @RequestBody OccupancyData data){
        dataService.upload(authorizationId,data);
    }
}
