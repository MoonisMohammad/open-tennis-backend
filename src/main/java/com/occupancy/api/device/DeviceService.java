package com.occupancy.api.device;

import com.google.zxing.WriterException;
import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRole;
import com.occupancy.api.facility.FacilityRepository;
import com.occupancy.api.device.qrcode.GenerateQRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
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

    public List<Device> getDevices(){return deviceRepository.findAll();}

    public Device getDeviceWithId(Long deviceId){
        Optional<Device> deviceOptional =deviceRepository.findById(deviceId);
        if(!deviceOptional.isPresent()){
            throw new IllegalStateException(
                    "device with id "+deviceId+"does not exist");
        }
        return  deviceOptional.get();
    }

    public List<Device> getDevicesWithFacilityId(Long facilityId) {
        return deviceRepository.findByFacilityId(facilityId);
    }


    public List<Device> getOwnedDevices() {
        AppUser appUser = getCurrentUser();
        return  deviceRepository.findByOwnerId((appUser.getOrganizationId()));
    }

    public String addNewDevice() throws IOException, WriterException {
        Device device = new Device();
        if(getCurrentUser().getAppUserRole()== AppUserRole.ADMIN) {
            deviceRepository.save(device);

        }else{
        throw new IllegalStateException(
                "Only admin can do this action");
        }

        GenerateQRCode g = new GenerateQRCode();

        System.out.println(device.getAuthorizationId());

        return GenerateQRCode.tryQr(device.getAuthorizationId());

    }

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

    @Transactional
    public void registerDevice(Device device){
        AppUser appUser = getCurrentUser();
        Long ownerId = appUser.getOrganizationId();
        Long facilityId = device.getFacilityId();
        String authorizationId = device.getAuthorizationId();
        int areasMonitored = device.getAreasMonitored();
        DeviceType deviceType = device.getDeviceType();
        String name = device.getName();

        Device d = deviceRepository.findByAuthorizationId(authorizationId);
        d.register(ownerId, facilityId, name,areasMonitored,deviceType);
    }

    public DeviceType[] getTypes(){
        return DeviceType.values();
    }

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


