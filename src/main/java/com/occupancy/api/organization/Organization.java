package com.occupancy.api.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
public class Organization {
    @Id
    @SequenceGenerator(
            name = "organization_sequence",
            sequenceName = "organization_sequence",
            allocationSize =1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "organization_sequence"

    )
    private Long id;
    private String name;

    public Organization(){}

    public Organization(String name) {
        this.name = name;
    }
}
