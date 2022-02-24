package com.occupancy.api.occupancyData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "data")
public class OccupancyDataViewController {

    private final OccupancyDataService dataService;

    @Autowired
    public OccupancyDataViewController(OccupancyDataService dataService){
        this.dataService = dataService;
    }

    @GetMapping(path = "hourlyAverage")
    public List<Double> timelyAverage(@RequestParam String deviceId,
                                      @RequestParam String referenceNumber,
                                      @RequestParam String dateTime){
        return dataService.getTimelyDataAverages(Long.valueOf(deviceId),Integer.valueOf(referenceNumber),dateTime);
    }

    @GetMapping(path = "dailyAverage")
    public List<Long> dailyAverage(@RequestParam String deviceId,
                                   @RequestParam String referenceNumber,
                                   @RequestParam String dateTime){
        return dataService.getDailyDataAverages(Long.valueOf(deviceId),Integer.valueOf(referenceNumber),dateTime);
    }
}
