package com.occupancy.api.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Device {
    @Id
    @SequenceGenerator(
            name = "device_sequence",
            sequenceName = "device_sequence",
            allocationSize =1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "device_sequence"

    )
    private Long id;
    //organization that owns the device
    private Long ownerId;
    private  Long facilityId;
    private String name;
    private String authorizationId;
    private boolean inUse;
    private  int areasMonitored;
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;
    private int[] currOccupancy;

    //This constructor creates a device which is not registered to any facility yet
    public Device(){
        this.inUse =false;
        authorizationId = createAuthId();
    }

    //constructor for testing to create devices
    public Device(Long ownerId,
                  Long facilityId,
                  String name,
                  int areasMonitored,
                  DeviceType deviceType){
        this.inUse = true;
        this.ownerId = ownerId;
        this.facilityId =facilityId;
        this.name = name;
        this.areasMonitored = areasMonitored;
        this.deviceType = deviceType;
        currOccupancy = new int[areasMonitored];
    }

    //ignore because we dont want the authentication id to be visible in our JSON
    @JsonIgnore
    public String getAuthorizationId(){return authorizationId;}

    public Device(String s) {authorizationId = s;}

    public void unregister(){
        this.inUse =false;
    }

    //register the device to a facility
    public void register(Long ownerId,
                         Long facilityId,
                         String name,
                         int areasMonitored,
                         DeviceType deviceType){
        this.inUse = true;
        this.ownerId = ownerId;
        this.facilityId =facilityId;
        this.name = name;
        this.areasMonitored = areasMonitored;
        this.deviceType = deviceType;
        currOccupancy = new int[areasMonitored];
    }

    public void setCurrOccupancy(int referenceNumber, int count){currOccupancy[referenceNumber] = count;}

    //creates a random authId for the device
    public String createAuthId(){
        int m = (int) Math.pow(10, 4);
        String r = Integer.toString( m + new Random().nextInt(9 * m));
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        System.out.println(datetime);
        return r + datetime;
    }

}
