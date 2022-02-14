package com.occupancy.api.data;


import com.occupancy.api.device.Device;
import com.occupancy.api.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.transaction.Transactional;
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

    @Transactional
    public void upload(String authorizationId,
                       Data data){
        Device device = deviceRepository.findByAuthorizationId(authorizationId);
        if (device == null){
            throw new IllegalStateException(
                "device with authorization Id "+authorizationId+" does not exist");
        }
        if (data.getRefrenceNumber() < 0 && data.getRefrenceNumber() >= device.getAreasMonitored()){
            throw new IllegalStateException(
                    "Reference number out of bounds for device");
        }
        device.setCurrOccupancy(data.getRefrenceNumber(), data.getCount());
        data.setFacilityId(device.getFacilityId());
        data.setDeviceId(device.getId());
        dataRepository.save(data);
    }



}
