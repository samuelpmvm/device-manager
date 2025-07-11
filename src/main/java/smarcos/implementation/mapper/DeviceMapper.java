package smarcos.implementation.mapper;

import com.model.device.DeviceCreationRequest;
import com.model.device.DeviceResponse;
import smarcos.implementation.entities.Device;

public final class DeviceMapper {

    private DeviceMapper() {}

    public static DeviceResponse toDeviceResponse(Device device) {
        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setId(device.getId());
        deviceResponse.setName(device.getName());
        deviceResponse.setBrand(device.getBrand());
        deviceResponse.setState(device.getState());
        deviceResponse.setCreationTime(device.getCreationTime());
        return deviceResponse;
    }

    public static Device toEntity(DeviceCreationRequest deviceCreationRequest) {
        Device device = new Device();
        device.setName(deviceCreationRequest.getName());
        device.setBrand(deviceCreationRequest.getBrand());
        device.setState(deviceCreationRequest.getState());
        return device;
    }
}
