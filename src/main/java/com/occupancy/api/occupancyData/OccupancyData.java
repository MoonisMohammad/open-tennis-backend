package com.occupancy.api.occupancyData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter

public class OccupancyData {
    @Id
    @SequenceGenerator(
            name = "data_sequence",
            sequenceName = "data_sequence",
            allocationSize =1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "data_sequence"

    )
    private Long id;
    private Long facilityId;
    private Long deviceId;
    private int count;
    private int referenceNumber;
    private int dayOfWeek;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    private LocalTime time;
    private LocalDate date;
    public OccupancyData(){}
    public OccupancyData(Long facilityId,
                         Long deviceId,
                         int count,
                         int referenceNumber,
                         DayOfWeek dayOfWeek,
                         LocalDateTime timeStamp){
        this.facilityId = facilityId;
        this.deviceId = deviceId;
        this.count = count;
        this.referenceNumber = referenceNumber;
        this.dayOfWeek = dayOfWeek.getValue();
        this.timeStamp = timeStamp;
        this.time = timeStamp.toLocalTime();
        this.date = timeStamp.toLocalDate();
    }

    //return the day of week the data was uploaded
    public DayOfWeek getDayOfWeek(){
        return DayOfWeek.of(dayOfWeek);
    }

}
