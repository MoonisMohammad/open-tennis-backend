package com.occupancy.api.device;

import com.google.zxing.WriterException;
import com.occupancy.api.facility.Facility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService){
        this.deviceService = deviceService;
    }

    @GetMapping(path = "{deviceId}")
    public Device getDeviceWithId(
            @PathVariable("deviceId") Long deviceId){
        return deviceService.getDeviceWithId(deviceId);
    }

    @GetMapping(path = "inFacility/{facilityId}")
    public List<Device> getDevicesWithFacilityId(
            @PathVariable("facilityId") Long facilityId){
        return deviceService.getDevicesWithFacilityId(facilityId);
    }

    @GetMapping(path = "owned")
    public List<Device> getOwnedDevices(){
        return deviceService.getOwnedDevices();
    }


    @PutMapping(path = "register")
    public void registerDevice(
                                @RequestBody Device device){
        deviceService.registerDevice(device);
    }

    @PostMapping
    public @ResponseBody String addNewDevice() throws IOException, WriterException {

        return deviceService.addNewDevice();
    }

    @PutMapping(path = "{deviceId}")
    public void updateDevice(
            @PathVariable("deviceId") Long deviceId,
            @RequestParam(required = false) String name){
        deviceService.updateDevice(deviceId,name);

    }

}
