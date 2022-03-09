package com.occupancy.api.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>{

    //find all devices with given user id
    List<Device> findByOwnerId(Long ownerId);

    //find all devices with given auth id
    Device findByAuthorizationId(String authorizationId);

    //find all devices with given facility id
    List<Device> findByFacilityId(Long facilityId);


}
