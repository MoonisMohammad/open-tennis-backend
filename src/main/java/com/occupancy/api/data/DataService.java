package com.occupancy.api.data;


import com.occupancy.api.average_calculator.AverageCalculator;
import com.occupancy.api.device.Device;
import com.occupancy.api.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
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

    public List<Long> getTimelyDataAverages(Long deviceId,
                                      Integer referenceNumber,
                                      String now){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.split("T")[0]+" "+now.split("T")[1];
        LocalDateTime nowDateTime = LocalDateTime.parse(formattedTime,formatter);
        LocalTime from = nowDateTime.toLocalTime();
        LocalTime  to  =from.plusHours(1);
        LocalDateTime thirtyDaysAgoDate = nowDateTime.minusDays(30);
        ArrayList<Long> weeklyAverage = new ArrayList<Long>();
        Long intervals = Long.valueOf(6);
        AverageCalculator averageCalculator = new AverageCalculator();
        Long sundayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber, DayOfWeek.valueOf("SUNDAY")),intervals);
        Long mondayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber,DayOfWeek.valueOf("MONDAY")),intervals);
        Long tuesdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber,DayOfWeek.valueOf("TUESDAY")),intervals);
        Long wednesdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber,DayOfWeek.valueOf("WEDNESDAY")),intervals);
        Long thursdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber,DayOfWeek.valueOf("THURSDAY")),intervals);
        Long fridayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber,DayOfWeek.valueOf("FRIDAY")),intervals);
        Long saturdayAverage = averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyDaysAgoDate,from,to,deviceId,referenceNumber,DayOfWeek.valueOf("SATURDAY")),intervals);
        weeklyAverage.add(sundayAverage);
        weeklyAverage.add(mondayAverage);
        weeklyAverage.add(tuesdayAverage);
        weeklyAverage.add(wednesdayAverage);
        weeklyAverage.add(thursdayAverage);
        weeklyAverage.add(fridayAverage);
        weeklyAverage.add(saturdayAverage);
        return weeklyAverage;
    }

    public List<Long> getDailyDataAverages(Long deviceId,
                                            Integer referenceNumber,
                                            String now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.split("T")[0]+" "+now.split("T")[1];
        LocalDateTime nowDateTime = LocalDateTime.parse(formattedTime,formatter);
        LocalDateTime thirtyDaysAgoDate = nowDateTime.minusDays(30);
        ArrayList<Long> weeklyAverage = new ArrayList<Long>();
        AverageCalculator averageCalculator = new AverageCalculator();
        Long sundayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber, DayOfWeek.valueOf("SUNDAY"));
        Long mondayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber,DayOfWeek.valueOf("MONDAY"));
        Long tuesdayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber,DayOfWeek.valueOf("TUESDAY"));
        Long wednesdayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber,DayOfWeek.valueOf("WEDNESDAY"));
        Long thursdayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber,DayOfWeek.valueOf("THURSDAY"));
        Long fridayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber,DayOfWeek.valueOf("FRIDAY"));
        Long saturdayAverage = dataRepository.findTotalCountLast30Days(thirtyDaysAgoDate,deviceId,referenceNumber,DayOfWeek.valueOf("SATURDAY"));
        weeklyAverage.add(sundayAverage);
        weeklyAverage.add(mondayAverage);
        weeklyAverage.add(tuesdayAverage);
        weeklyAverage.add(wednesdayAverage);
        weeklyAverage.add(thursdayAverage);
        weeklyAverage.add(fridayAverage);
        weeklyAverage.add(saturdayAverage);
        return weeklyAverage;
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
        data.setDayOfWeek(data.getTimeStamp().getDayOfWeek());
        data.setDate(data.getTimeStamp().toLocalDate());
        data.setTime(data.getTimeStamp().toLocalTime());
        dataRepository.save(data);
    }



}
