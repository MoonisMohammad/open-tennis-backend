package com.occupancy.api.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>{

    List<Device> findByOwnerId(Long ownerId);

    Device findByAuthorizationId(String authorizationId);

    List<Device> findByFacilityId(Long facilityId);


}
