package com.occupancy.api.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "data")
public class DataViewController {

    private final DataService dataService;

    @Autowired
    public DataViewController(DataService dataService){
        this.dataService = dataService;
    }

    @GetMapping(path = "average")
    public List<Data> test(@RequestParam String deviceId,
                           @RequestParam String referenceNumber){
        return dataService.getDataAverages(Long.valueOf(deviceId),Integer.valueOf(referenceNumber));
    }
}
