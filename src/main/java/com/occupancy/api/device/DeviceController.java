package com.occupancy.api.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import com.occupancy.api.facility.Facility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

    //return device with given id
    @GetMapping(path = "{deviceId}")
    public Device getDeviceWithId(@PathVariable("deviceId") Long deviceId){
        return deviceService.getDeviceWithId(deviceId);
    }

    //get devices with a specific
    @GetMapping(path = "inFacility/{facilityId}")
    public List<Device> getDevicesWithFacilityId(@PathVariable("facilityId") Long facilityId){
        return deviceService.getDevicesWithFacilityId(facilityId);
    }

    //get devices owned by user
    @GetMapping(path = "owned")
    public List<Device> getOwnedDevices(){
        return deviceService.getOwnedDevices();
    }

    //get all device type
    @GetMapping(path = "types")
    public DeviceType[] getTypes(){
        return deviceService.getTypes();
    }

    //registers a device to a facility is request params are valid
    @PutMapping(path = "register")
    public void registerDevice(@RequestBody  DeviceRegistrationRequest deviceRegistrationRequest) throws JsonProcessingException {
        deviceService.registerDevice(deviceRegistrationRequest);
    }

    //used by admin to create a new device
    @PostMapping
    public @ResponseBody String addNewDevice() throws IOException, WriterException {
        return deviceService.addNewDevice();
    }

    @PutMapping(path = "{deviceId}")
    public void updateDevice(@PathVariable("deviceId") Long deviceId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Integer areasMonitored,
                             @RequestParam(required = false) DeviceType deviceType){

        deviceService.updateDevice(deviceId, name, areasMonitored, deviceType);

    }

}
