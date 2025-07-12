package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
import com.model.device.DevicePartiallyUpdateRequest;
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
import smarcos.implementation.exceptions.DeviceInUseException;
import smarcos.implementation.exceptions.DeviceNotFoundException;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.repository.DeviceRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void updateDeviceInUseFails() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.IN_USE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        deviceCreationRequest.setName("Update");

        assertThrows(DeviceInUseException.class, () -> deviceService.updateDevice(id, deviceCreationRequest));
    }

    @Test
    void updateDeviceNotFoundFails() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(id, deviceCreationRequest));
    }

    @Test
    void partiallyUpdateDeviceSuccess() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName(DEVICE_NAME);
        var device = new Device();
        device.setName(DEVICE_NAME);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var partiallyUpdateDevice = deviceService.partiallyUpdateDevice(id, devicePartiallyUpdateRequest);
        assertNotNull(partiallyUpdateDevice);
        assertEquals(DEVICE_NAME, partiallyUpdateDevice.getName());
    }

    @Test
    void partiallyUpdateDeviceNotFoundFails() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName(DEVICE_NAME);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.partiallyUpdateDevice(id, devicePartiallyUpdateRequest));
    }

    @Test
    void updateNameOrBrandOfDeviceInUseFails() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName("Name");
        var device = new Device();
        device.setName(DEVICE_NAME);
        device.setState(StateDto.IN_USE);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        assertThrows(DeviceInUseException.class, () -> deviceService.partiallyUpdateDevice(id, devicePartiallyUpdateRequest));

        devicePartiallyUpdateRequest.setName(null);
        devicePartiallyUpdateRequest.setBrand("Brand");

        assertThrows(DeviceInUseException.class, () -> deviceService.partiallyUpdateDevice(id, devicePartiallyUpdateRequest));
    }

    @Test
    void updateStateOfDeviceInUseSuccess() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setState(StateDto.INACTIVE);
        var device = new Device();
        device.setState(StateDto.IN_USE);
        var id = UUID.fromString(ID);
        Mockito.when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var partiallyUpdateDevice = deviceService.partiallyUpdateDevice(id, devicePartiallyUpdateRequest);
        assertNotNull(partiallyUpdateDevice);
        assertEquals(StateDto.INACTIVE, partiallyUpdateDevice.getState());
    }
}
