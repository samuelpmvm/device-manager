package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
import com.model.device.DevicePartiallyUpdateRequest;
import com.model.device.StateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import smarcos.implementation.PostgresIntegrationTest;
import smarcos.implementation.exceptions.DeviceNotFoundException;
import smarcos.implementation.repository.DeviceRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
@Tag("integration")
class DeviceServiceIntegrationTest extends PostgresIntegrationTest {


    private static final String UPDATED_DEVICE_NAME = "Updated Device";
    private static final String UPDATED_BRAND = "Updated Brand";
    @Autowired
    private DeviceRepository deviceRepository;

    private static final String DEVICE_NAME = "Device";
    private static final String DEVICE_BRAND = "Brand";

    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService(deviceRepository);
    }


     @Test
     void createDeviceSuccess() {
         var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
            var createdDevice = deviceService.createDevice(deviceCreationRequest);
            assertNotNull(createdDevice);
            assertNotNull(createdDevice.getId());
            assertEquals(DEVICE_NAME, createdDevice.getName());
            assertEquals(DEVICE_BRAND, createdDevice.getBrand());
            assertEquals(StateDto.AVAILABLE, createdDevice.getState());
            assertNotNull(createdDevice.getCreationTime());
     }

    @Test
    void updateDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var createdDevice = deviceService.createDevice(deviceCreationRequest);
        assertNotNull(createdDevice);
        assertNotNull(createdDevice.getId());

        var updatedDeviceRequest = new DeviceCreationRequest(UPDATED_DEVICE_NAME, UPDATED_BRAND, StateDto.IN_USE);
        var updatedDevice = deviceService.updateDevice(createdDevice.getId(), updatedDeviceRequest);
        assertEquals(createdDevice.getId(), updatedDevice.getId());
        assertEquals(UPDATED_DEVICE_NAME, updatedDevice.getName());
        assertEquals(UPDATED_BRAND, updatedDevice.getBrand());
        assertEquals(StateDto.IN_USE, updatedDevice.getState());
        assertEquals(createdDevice.getCreationTime(), updatedDevice.getCreationTime());
    }

    @Test
    void partiallyUpdateDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var createdDevice = deviceService.createDevice(deviceCreationRequest);
        assertNotNull(createdDevice);
        assertNotNull(createdDevice.getId());

        var devicePartiallyUpdateRequest = new DevicePartiallyUpdateRequest();
        devicePartiallyUpdateRequest.setName(UPDATED_DEVICE_NAME);
        var partiallyUpdateDevice = deviceService.partiallyUpdateDevice(createdDevice.getId(), devicePartiallyUpdateRequest);
        assertEquals(createdDevice.getId(), partiallyUpdateDevice.getId());
        assertEquals(UPDATED_DEVICE_NAME, partiallyUpdateDevice.getName());
        assertEquals(createdDevice.getBrand(), partiallyUpdateDevice.getBrand());
        assertEquals(createdDevice.getState(), partiallyUpdateDevice.getState());
        assertEquals(createdDevice.getCreationTime(), partiallyUpdateDevice.getCreationTime());
    }

    @Test
    void getDeviceByIdSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var createdDevice = deviceService.createDevice(deviceCreationRequest);
        assertNotNull(createdDevice);
        assertNotNull(createdDevice.getId());

        var existingDevice = deviceService.getDeviceById(createdDevice.getId());
        assertEquals(createdDevice.getId(), existingDevice.getId());
        assertEquals(createdDevice.getName(), existingDevice.getName());
        assertEquals(createdDevice.getBrand(), existingDevice.getBrand());
        assertEquals(createdDevice.getState(), existingDevice.getState());
        assertEquals(createdDevice.getCreationTime(), existingDevice.getCreationTime());
    }

    @Test
    void getAllDevices() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        deviceService.createDevice(deviceCreationRequest);
        deviceService.createDevice(deviceCreationRequest);

        var existingDevices = deviceService.getAllDevices();
        assertEquals(2, existingDevices.size());
    }

    @Test
    void deleteDeviceSuccess() {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var createdDevice = deviceService.createDevice(deviceCreationRequest);
        assertNotNull(createdDevice);
        var id = createdDevice.getId();
        assertNotNull(id);

        var existingDevice = deviceService.getDeviceById(id);
        assertNotNull(existingDevice);
        deviceService.deleteDevice(existingDevice.getId());
        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(id));
    }
}