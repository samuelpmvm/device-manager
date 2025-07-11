package smarcos.implementation.mapper;

import com.model.device.DeviceDto;
import com.model.device.StateDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import smarcos.implementation.entities.Device;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class DeviceMapperTest {

    @Test
    void toDto() {
        var device = new Device();
        device.setId(device.getId());
        device.setName("Test Device");
        device.setBrand("Test Brand");
        device.setState(StateDto.IN_USE);
        var deviceDto = DeviceMapper.toDto(device);
        assertNotNull(deviceDto);
        assertEquals(device.getId(), deviceDto.getId());
        assertEquals(device.getName(), deviceDto.getName());
        assertEquals(device.getBrand(), deviceDto.getBrand());
        assertEquals(device.getState(), deviceDto.getState());
    }

    @Test
    void toEntity() {
        var deviceDto = new DeviceDto();
        deviceDto.setName("Test Device");
        deviceDto.setBrand("Test Brand");
        deviceDto.setState(StateDto.IN_USE);
        var device = DeviceMapper.toEntity(deviceDto);
        assertNotNull(device);
        assertEquals(deviceDto.getName(), device.getName());
        assertEquals(deviceDto.getBrand(), device.getBrand());
        assertEquals(deviceDto.getState(), device.getState());
    }
}