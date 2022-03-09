package com.occupancy.api.occupancyData;

import com.occupancy.api.occupancyData.average_calculator.AverageCalculator;
import com.occupancy.api.device.Device;
import com.occupancy.api.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OccupancyDataService {

    private final OccupancyDataRepository dataRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public OccupancyDataService(OccupancyDataRepository dataRepository,
                                DeviceRepository deviceRepository){
        this.dataRepository = dataRepository;
        this.deviceRepository = deviceRepository;
    }

    //gets average of weekdays based on time and provide date
    public List<Double> getTimelyDataAverages(Long deviceId,
                                              Integer referenceNumber,
                                              String now){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.split("T")[0]+" "+now.split("T")[1];
        LocalDateTime nowDateTime = LocalDateTime.parse(formattedTime,formatter);
        LocalTime from = nowDateTime.toLocalTime();
        LocalTime to  = from.plusHours(2);
        LocalDateTime thirtyFiveDaysAgoDate = nowDateTime.minusDays(35);
        //number of weeks multiplied by number of 10 hours intervals in 2 hours
        Long intervals = Long.valueOf(ChronoUnit.WEEKS.between(thirtyFiveDaysAgoDate,nowDateTime))*12;
        AverageCalculator averageCalculator = new AverageCalculator();
        return averageCalculator.calculateAverage(dataRepository.findAllWithDateAfter(thirtyFiveDaysAgoDate,from,to,deviceId,referenceNumber),intervals);
    }

    //uploads data to the database if it's a valid authorization id
    @Transactional
    public void upload(String authorizationId,
                       OccupancyData data){
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
        data.setDayOfWeek(data.getTimeStamp().getDayOfWeek().getValue());
        data.setDate(data.getTimeStamp().toLocalDate());
        data.setTime(data.getTimeStamp().toLocalTime());
        dataRepository.save(data);
    }

    //gets all occupancy for device between two dates
    public List<OccupancyData> getBetween(String fromDateTime,
                                 String toDateTime,
                                 Long deviceId,
                                 int referenceNumber) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedFromDateTime = fromDateTime.split("T")[0] + " " + fromDateTime.split("T")[1];
        String formattedToDateTime = toDateTime.split("T")[0] + " " + toDateTime.split("T")[1];
        LocalDateTime from = LocalDateTime.parse(formattedFromDateTime,formatter);
        LocalDateTime to = LocalDateTime.parse(formattedToDateTime,formatter);
        return dataRepository.getBetween(from,to,deviceId,referenceNumber);
    }
}
