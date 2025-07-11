package smarcos.implementation.services;

import com.model.device.DeviceDto;
import com.model.device.StateDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import smarcos.implementation.entities.Device;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.repository.DeviceRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class DeviceServiceTest {

    @InjectMocks
    private DeviceService deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    private static final String DEVICE_NAME = "Device";
    private static final String DEVICE_BRAND = "Brand";

    @Test
    void createDeviceSuccess() {
        var deviceDto = new DeviceDto(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceDto);
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var createdDevice = deviceService.createDevice(deviceDto);
        assertNotNull(createdDevice);
        assertEquals(DEVICE_NAME, createdDevice.getName());
        assertEquals(DEVICE_BRAND, createdDevice.getBrand());
        assertEquals(StateDto.AVAILABLE, createdDevice.getState());
    }
}
