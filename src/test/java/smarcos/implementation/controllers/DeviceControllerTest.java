package smarcos.implementation.controllers;

import com.model.device.DeviceCreationRequest;
import com.model.device.DevicePartiallyUpdateRequest;
import com.model.device.StateDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import smarcos.implementation.entities.Device;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.services.DeviceService;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeviceController.class)
@Tag("unit")
class DeviceControllerTest {
    public static final String DEVICE_NAME = "Device Name";
    public static final String DEVICE_BRAND = "Device Brand";
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    private static final String ID = "0d75f424-0ee4-48f8-83cd-c2067ab0c9bb";

    @Test
    void createDeviceSuccess() throws Exception {
        var deviceDto = new DeviceCreationRequest("Device Name", "Device Brand", StateDto.AVAILABLE);
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
                .andExpect(jsonPath("$.creationTime").value(startsWith(device.getCreationTime().toString().substring(0, 23))));
    }

    @Test
    void updateDeviceSuccess() throws Exception {
        var deviceCreationRequest = new DeviceCreationRequest(DEVICE_NAME, DEVICE_BRAND, StateDto.AVAILABLE);
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        var time = OffsetDateTime.now();
        var id = UUID.fromString(ID);
        device.setId(id);
        device.setCreationTime(time);
        Mockito.when(deviceService.updateDevice(id, deviceCreationRequest)).thenReturn(device);

        var request = MockMvcRequestBuilders
                .put("/api/v1/devices/" + ID)
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name",
                          "brand": "Device Brand",
                          "state": "available"
                        }""");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.name").value(device.getName()))
                .andExpect(jsonPath("$.brand").value(device.getBrand()))
                .andExpect(jsonPath("$.state").value(device.getState().getValue()))
                .andExpect(jsonPath("$.creationTime").value(startsWith(device.getCreationTime().toString().substring(0, 23))));
    }

    @Test
    void partiallyUpdateDeviceSuccess() throws Exception {
        var device = new Device();
        var time = OffsetDateTime.now();
        var id = UUID.fromString(ID);
        device.setId(id);
        device.setName(DEVICE_NAME);
        device.setState(StateDto.AVAILABLE);
        device.setBrand(DEVICE_BRAND);
        device.setCreationTime(time);
        Mockito.when(deviceService.partiallyUpdateDevice(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(DevicePartiallyUpdateRequest.class))).thenReturn(device);

        var request = MockMvcRequestBuilders
                .patch("/api/v1/devices/" + ID)
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name"
                        }""");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(device.getId().toString()))
                .andExpect(jsonPath("$.name").value(device.getName()))
                .andExpect(jsonPath("$.brand").value(device.getBrand()))
                .andExpect(jsonPath("$.state").value(device.getState().getValue()))
                .andExpect(jsonPath("$.creationTime").value(startsWith(device.getCreationTime().toString().substring(0, 23))));
    }
}