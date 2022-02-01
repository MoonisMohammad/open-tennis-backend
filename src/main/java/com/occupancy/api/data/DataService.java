package com.occupancy.api.data;


import com.occupancy.api.device.Device;
import com.occupancy.api.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
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

    public void upload(String authorizationId,
                       Data data){
        Device device = deviceRepository.findByAuthorizationId(authorizationId);
        if (device == null){
            throw new IllegalStateException(
                "device with authorization Id "+authorizationId+"does not exist");
        }

        data.setFacilityId(device.getFacilityId());
        data.setDeviceId(device.getId());

        dataRepository.save(data);
    }



}
