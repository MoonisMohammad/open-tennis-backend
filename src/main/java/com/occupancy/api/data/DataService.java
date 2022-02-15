package com.occupancy.api.data;


import com.occupancy.api.average_calculator.AverageCalculator;
import com.occupancy.api.device.Device;
import com.occupancy.api.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public DataService(DataRepository dataRepository,
                       DeviceRepository deviceRepository){
        this.dataRepository = dataRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<Integer> getDataAverages(Long deviceId,Integer referenceNumber){
        LocalDateTime thirtyDaysAgoDate = LocalDateTime.now().minusDays(30);
        ArrayList<Integer> weeklyAverage = new ArrayList<Integer>();
        AverageCalculator averageCalculator = new AverageCalculator();
        sundayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));
        mondayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));
        tuesdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));
        wednesdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));
        thursdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));
        fridayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));
        saturdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber));

        return dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,deviceId,referenceNumber);
    }
    @Transactional
    public void upload(String authorizationId,
                       Data data){
        Device device = deviceRepository.findByAuthorizationId(authorizationId);
        if (device == null){
            throw new IllegalStateException(
                "device with authorization Id "+authorizationId+" does not exist");
        }
        if (data.getReferenceNumber() < 0 && data.getReferenceNumber() >= device.getAreasMonitored()){
            throw new IllegalStateException(
                    "Reference number out of bounds for device");
        }
        device.setCurrOccupancy(data.getReferenceNumber(), data.getCount());
        data.setFacilityId(device.getFacilityId());
        data.setDeviceId(device.getId());
        dataRepository.save(data);
    }



}
