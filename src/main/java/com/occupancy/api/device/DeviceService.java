package com.occupancy.api.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRole;
import com.occupancy.api.facility.FacilityRepository;
import com.occupancy.api.device.qrcode.GenerateQRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final FacilityRepository facilityRepository;

    @Autowired
    public  DeviceService(DeviceRepository deviceRepository,
                          FacilityRepository facilityRepository){
        this.deviceRepository = deviceRepository;
        this.facilityRepository =facilityRepository;
    }

    //gets all devices
    public List<Device> getDevices(){return deviceRepository.findAll();}

    //gets device with specific id
    public Device getDeviceWithId(Long deviceId){
        Optional<Device> deviceOptional =deviceRepository.findById(deviceId);
        if(!deviceOptional.isPresent()){
            throw new IllegalStateException(
                    "device with id "+deviceId+"does not exist");
        }
        return  deviceOptional.get();
    }

    //gets all devices that have the specific facility id
    public List<Device> getDevicesWithFacilityId(Long facilityId) {
        return deviceRepository.findByFacilityId(facilityId);
    }

    //gets all devices owned by current users organization
    public List<Device> getOwnedDevices() {
        AppUser appUser = getCurrentUser();
        return  deviceRepository.findByOwnerId((appUser.getOrganizationId()));
    }

    //adds a new unregistered device to to database
    public String addNewDevice() throws IOException, WriterException {
        Device device = new Device();
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            deviceRepository.save(device);

        }else{
        throw new IllegalStateException(
                "Only admin can do this action");
        }

        GenerateQRCode g = new GenerateQRCode();

        System.out.println("Generated QR Code"+ device.getAuthorizationId());

        return GenerateQRCode.tryQr(device.getAuthorizationId());

    }

    //Updates device info
    @Transactional
    public void updateDevice(Long deviceId,
                             String name,
                             Integer areasMonitored,
                             DeviceType deviceType) {
        AppUser appUser = getCurrentUser();
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                        new IllegalStateException("Device with id " + deviceId + "does not exist"));
        Long deviceOwnerId = device.getOwnerId();
        Long facilityOwnerId = facilityRepository.findById(device.getFacilityId()).get().getOwnerId();
        if (!appUser.getOrganizationId().equals(deviceOwnerId)){
            throw new IllegalStateException(
                    "device with id "+deviceId+"not owned by user");
        }if(!appUser.getOrganizationId().equals(facilityOwnerId)){
            throw new IllegalStateException(
                    "facility not owned by user");
        }if(name != null && name.length()>0 && !Objects.equals(device.getName(),name)) {
            device.setName(name);
        }if(areasMonitored != null ) {
            device.setName(name);
        }if(deviceType != null ) {
            device.setDeviceType(deviceType);
        }
    }

    //registers device to users organization
    @Transactional
    public void registerDevice(DeviceRegistrationRequest deviceRegistrationRequest) throws JsonProcessingException {
        AppUser appUser = getCurrentUser();
        Long ownerId = appUser.getOrganizationId();
        Long facilityId = deviceRegistrationRequest.getFacilityId();
        String authorizationId = deviceRegistrationRequest.getAuthorizationId();
        Integer areasMonitored = deviceRegistrationRequest.getAreasMonitored();
        DeviceType deviceType = deviceRegistrationRequest.getDeviceType();
        String name = deviceRegistrationRequest.getName();
        Device device = deviceRepository.findByAuthorizationId(authorizationId);
        if(device!=null){
            System.out.println(device.getAreasMonitored() +"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            device.register(ownerId, facilityId, name,areasMonitored,deviceType);
        }
        System.out.println(authorizationId+"{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
    }

    //gives list of device types
    public DeviceType[] getTypes(){
        return DeviceType.values();
    }

    //return current user
    public AppUser getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser;
        if (principal instanceof AppUser) {
            appUser = ((AppUser)principal);
            return appUser;
        }else {
            return null;
        }
    }

}


