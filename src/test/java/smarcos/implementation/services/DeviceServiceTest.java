package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
import com.model.device.DevicePartiallyUpdateRequest;
import com.model.device.DeviceState;
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

import java.util.List;
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
    private static final UUID ID = UUID.fromString("0d75f424-0ee4-48f8-83cd-c2067ab0c9bb");

    @Test
    void createDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var createdDevice = deviceService.createDevice(deviceCreationRequest);
        assertNotNull(createdDevice);
        assertEquals(DEVICE_NAME, createdDevice.getName());
        assertEquals(DEVICE_BRAND, createdDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, createdDevice.getState());
    }

    @Test
    void updateDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var updateDevice = deviceService.updateDevice(ID, deviceCreationRequest);
        assertNotNull(updateDevice);
        assertEquals(DEVICE_NAME, updateDevice.getName());
        assertEquals(DEVICE_BRAND, updateDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, updateDevice.getState());
    }

    @Test
    void updateDeviceInUseFails() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.IN_USE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));
        deviceCreationRequest.setName("Update");

        assertThrows(DeviceInUseException.class, () -> deviceService.updateDevice(ID, deviceCreationRequest));
    }

    @Test
    void updateDeviceNotFoundFails() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(ID, deviceCreationRequest));
    }

    @Test
    void partiallyUpdateDeviceSuccess() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName(DEVICE_NAME);
        var device = new Device();
        device.setName(DEVICE_NAME);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var partiallyUpdateDevice = deviceService.partiallyUpdateDevice(ID, devicePartiallyUpdateRequest);
        assertNotNull(partiallyUpdateDevice);
        assertEquals(DEVICE_NAME, partiallyUpdateDevice.getName());
    }

    @Test
    void partiallyUpdateDeviceNotFoundFails() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName(DEVICE_NAME);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.partiallyUpdateDevice(ID, devicePartiallyUpdateRequest));
    }

    @Test
    void updateNameOrBrandOfDeviceInUseFails() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName("Name");
        var device = new Device();
        device.setName(DEVICE_NAME);
        device.setState(DeviceState.IN_USE);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));
        assertThrows(DeviceInUseException.class, () -> deviceService.partiallyUpdateDevice(ID, devicePartiallyUpdateRequest));

        devicePartiallyUpdateRequest.setName(null);
        devicePartiallyUpdateRequest.setBrand("Brand");

        assertThrows(DeviceInUseException.class, () -> deviceService.partiallyUpdateDevice(ID, devicePartiallyUpdateRequest));
    }

    @Test
    void updateStateOfDeviceInUseSuccess() {
        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setState(DeviceState.INACTIVE);
        var device = new Device();
        device.setState(DeviceState.IN_USE);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));
        Mockito.when(deviceRepository.save(ArgumentMatchers.any(Device.class))).thenReturn(device);

        var partiallyUpdateDevice = deviceService.partiallyUpdateDevice(ID, devicePartiallyUpdateRequest);
        assertNotNull(partiallyUpdateDevice);
        assertEquals(DeviceState.INACTIVE, partiallyUpdateDevice.getState());
    }

    @Test
    void getDeviceByIdSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));

        var existingDevice = deviceService.getDeviceById(ID);
        assertNotNull(existingDevice);
        assertEquals(DEVICE_NAME, existingDevice.getName());
        assertEquals(DEVICE_BRAND, existingDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, existingDevice.getState());
    }

    @Test
    void getDeviceByIdNotFoundFails() {
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(ID));
    }

    @Test
    void findAllDevices() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findAll()).thenReturn(List.of(device));

        var existingDevices = deviceService.findDevices(null, null);
        assertNotNull(existingDevices);
        var existingDevice = existingDevices.getFirst();
        assertEquals(DEVICE_NAME, existingDevice.getName());
        assertEquals(DEVICE_BRAND, existingDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, existingDevice.getState());
    }

    @Test
    void deleteDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));
        deviceService.deleteDevice(ID);

        Mockito.verify(deviceRepository, Mockito.times(1)).delete(device);
    }

    @Test
    void deleteDeviceInUseFails() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.IN_USE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.of(device));

        assertThrows(DeviceInUseException.class, () -> deviceService.deleteDevice(ID));
    }

    @Test
    void deleteDeviceNotFoundFails() {
        Mockito.when(deviceRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.deleteDevice(ID));
    }

    @Test
    void findDevicesByBrand() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findByBrand(DEVICE_BRAND)).thenReturn(List.of(device));

        var existingDevices = deviceService.findDevices(null, DEVICE_BRAND);
        assertNotNull(existingDevices);
        var existingDevice = existingDevices.getFirst();
        assertEquals(DEVICE_NAME, existingDevice.getName());
        assertEquals(DEVICE_BRAND, existingDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, existingDevice.getState());
    }

    @Test
    void findDevicesByState() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(List.of(device));

        var existingDevices = deviceService.findDevices(DeviceState.AVAILABLE, null);
        assertNotNull(existingDevices);
        var existingDevice = existingDevices.getFirst();
        assertEquals(DEVICE_NAME, existingDevice.getName());
        assertEquals(DEVICE_BRAND, existingDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, existingDevice.getState());
    }

    @Test
    void findDevicesReturnsNothing() {
        Mockito.when(deviceRepository.findAll()).thenReturn(List.of());

        var existingDevices = deviceService.findDevices(null, null);
        assertNotNull(existingDevices);
        assertEquals(0, existingDevices.size());
    }

    @Test
    void findDevicesByStateAndBrand() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, DeviceState.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        Mockito.when(deviceRepository.findByBrandAndState(DEVICE_BRAND, DeviceState.AVAILABLE)).thenReturn(List.of(device));

        var existingDevices = deviceService.findDevices(DeviceState.AVAILABLE, DEVICE_BRAND);
        assertNotNull(existingDevices);
        var existingDevice = existingDevices.getFirst();
        assertEquals(DEVICE_NAME, existingDevice.getName());
        assertEquals(DEVICE_BRAND, existingDevice.getBrand());
        assertEquals(DeviceState.AVAILABLE, existingDevice.getState());
    }
}
