package com.occupancy.api.device;

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
    private Long ownerId; //organization that owns the device
    private  Long facilityId;
    private String name;
    private String authorizationId;
    private boolean inUse;
    private  int areasMonitored;

    public Device(){

        this.inUse =false;
        authorizationId = createAuthId();
    }

    public void unregister(){
        this.inUse =false;
    }

    public void register(Long ownerId,Long facilityId,String name,int areasMonitored){
        this.inUse = true;
        this.ownerId = ownerId;
        this.facilityId =facilityId;
        this.name = name;
        this.areasMonitored =areasMonitored;
    }

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