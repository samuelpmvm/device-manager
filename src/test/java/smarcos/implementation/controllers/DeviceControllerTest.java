package smarcos.implementation.controllers;

import com.model.device.DeviceDto;
import com.model.device.StateDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.services.DeviceService;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeviceController.class)
@Tag("unit")
class DeviceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    private static final String ID = "0d75f424-0ee4-48f8-83cd-c2067ab0c9bb";

    @Test
    void createDeviceSuccess() throws Exception {
        var deviceDto = createDeviceDto();
        var device = DeviceMapper.toEntity(deviceDto);
        var time = OffsetDateTime.now();
        device.setId(UUID.fromString(ID));
        device.setCreationTime(time);
        Mockito.when(deviceService.createDevice(deviceDto)).thenReturn(device);

        var request = MockMvcRequestBuilders
                .post("/api/v1/devices")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name",
                          "brand": "Device Brand",
                          "state": "available"
                        }""");
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.name").value(device.getName()))
                .andExpect(jsonPath("$.brand").value(device.getBrand()))
                .andExpect(jsonPath("$.state").value(device.getState().getValue()))
                .andExpect(jsonPath("$.creationTime").value(device.getCreationTime().toString()));
    }

    private DeviceDto createDeviceDto() {
        return new DeviceDto("Device Name", "Device Brand", StateDto.AVAILABLE);
    }

}