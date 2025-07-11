package smarcos.implementation.mapper;

import com.model.device.DeviceDto;
import smarcos.implementation.entities.Device;

public final class DeviceMapper {

    private DeviceMapper() {}

    public static DeviceDto toDto(Device device) {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(device.getId());
        deviceDto.setName(device.getName());
        deviceDto.setBrand(device.getBrand());
        deviceDto.setState(device.getState());
        deviceDto.setCreationTime(device.getCreationTime());
        return deviceDto;
    }

    public static Device toEntity(DeviceDto deviceDto) {
        Device device = new Device();
        device.setId(deviceDto.getId());
        device.setName(deviceDto.getName());
        device.setBrand(deviceDto.getBrand());
        device.setState(deviceDto.getState());
        device.setCreationTime(deviceDto.getCreationTime());
        return device;
    }
}
