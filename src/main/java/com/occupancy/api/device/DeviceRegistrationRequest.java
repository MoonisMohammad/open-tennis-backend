package com.occupancy.api.device;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DeviceRegistrationRequest {
    public Long facilityId;
    public  String name;
    public String authorizationId;
    public Integer areasMonitored;
    public DeviceType deviceType;
}
