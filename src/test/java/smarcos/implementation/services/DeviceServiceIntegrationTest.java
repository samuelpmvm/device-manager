package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
import com.model.device.StateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import smarcos.implementation.PostgresIntegrationTest;
import smarcos.implementation.repository.DeviceRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertEquals(StateDto.IN_USE, createdDevice.getState());
        assertEquals(createdDevice.getCreationTime(), updatedDevice.getCreationTime());
    }




}