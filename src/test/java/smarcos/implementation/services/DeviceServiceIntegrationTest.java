package smarcos.implementation.services;

import com.model.device.DeviceDto;
import com.model.device.StateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import smarcos.implementation.PostgresIntegrationTest;
import smarcos.implementation.repository.DeviceRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Tag("integration")
class DeviceServiceIntegrationTest extends PostgresIntegrationTest {


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
         var deviceDto = new DeviceDto(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
            var createdDevice = deviceService.createDevice(deviceDto);
            assertNotNull(createdDevice);
            assertNotNull(createdDevice.getId());
            assertEquals(DEVICE_NAME, createdDevice.getName());
            assertEquals(DEVICE_BRAND, createdDevice.getBrand());
            assertEquals(StateDto.AVAILABLE, createdDevice.getState());
            assertNotNull(createdDevice.getCreationTime());
     }

}