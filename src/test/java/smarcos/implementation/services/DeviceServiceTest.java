package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
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

import java.util.Optional;
import java.util.UUID;

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
    private static final String ID = "0d75f424-0ee4-48f8-83cd-c2067ab0c9bb";

    @Test
    void createDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var createdDevice = deviceService.createDevice(deviceCreationRequest);
        assertNotNull(createdDevice);
        assertEquals(DEVICE_NAME, createdDevice.getName());
        assertEquals(DEVICE_BRAND, createdDevice.getBrand());
        assertEquals(StateDto.AVAILABLE, createdDevice.getState());
    }

    @Test
    void updateDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var updateDevice = deviceService.updateDevice(id, deviceCreationRequest);
        assertNotNull(updateDevice);
        assertEquals(DEVICE_NAME, updateDevice.getName());
        assertEquals(DEVICE_BRAND, updateDevice.getBrand());
        assertEquals(StateDto.AVAILABLE, updateDevice.getState());
    }
}
