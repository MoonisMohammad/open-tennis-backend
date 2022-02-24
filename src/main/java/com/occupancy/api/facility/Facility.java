package com.occupancy.api.facility;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Facility {
    @Id
    @SequenceGenerator(
            name = "facility_sequence",
            sequenceName = "facility_sequence",
            allocationSize =1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "facility_sequence"

    )
    private Long id;
    private Long ownerId; //organization that owns the facility
    private String name;
    private String city;
    private Double latitude;
    private Double longitude;

    public Facility(){}

    public Facility(Long ownerId,
                    String name,
                    String city,
                    Double latitude,
                    Double longitude) {
        this.ownerId = ownerId;
        this.name = name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
}
