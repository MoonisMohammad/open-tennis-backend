package com.occupancy.api.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter

public class Data {
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
    private DayOfWeek dayOfWeek;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    private LocalTime time;
    private LocalDate date;
    public Data(){}
    public Data(Long facilityId,
                Long deviceId,
                int count,
                int referenceNumber,
                DayOfWeek dayOfWeek,
                LocalDateTime timeStamp){
        this.facilityId = facilityId;
        this.deviceId =deviceId;
        this.count =count;
        this.referenceNumber =referenceNumber;
        this.dayOfWeek =dayOfWeek;
        this.timeStamp =timeStamp;
        this.time = timeStamp.toLocalTime();
        this.date = timeStamp.toLocalDate();
    }

}
