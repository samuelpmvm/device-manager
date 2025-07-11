package smarcos.implementation.exceptions;

import com.model.device.ApiErrorCode;
import com.model.device.DeviceCreationRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import smarcos.implementation.controllers.DeviceController;
import smarcos.implementation.services.DeviceService;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeviceController.class)
@Tag("unit")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    @Test
    void handleHttpMessageNotReadableException() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/api/v1/devices")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.BAD_REQUEST.getValue()));
    }

    @Test
    void handleMethodArgumentNotValidException() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/api/v1/devices")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("{}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.BAD_REQUEST.getValue()));
    }

    @Test
    void handleHttpMediaTypeNotSupportedException() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/api/v1/devices")
                .content("{}");

        mockMvc.perform(request)
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.MEDIA_TYPE_NOT_SUPPORTED.getValue()));
    }

    @Test
    void handleHttpRequestMethodNotSupportedException() throws Exception {
        var request = MockMvcRequestBuilders
                .delete("/api/v1/devices")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name",
                          "brand": "Device Brand",
                          "state": "available"
                        }""");

        mockMvc.perform(request)
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.METHOD_NOT_ALLOWED.getValue()));
    }

    @Test
    void handleGeneral() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(deviceService).createDevice(ArgumentMatchers.any(DeviceCreationRequest.class));
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
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.INTERNAL_SERVER_ERROR.getValue()));
    }

    @Test
    void handleNoResourceFoundException() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/devices")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name",
                          "brand": "Device Brand",
                          "state": "available"
                        }""");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.RESOURCE_NOT_FOUND.getValue()));
    }

    @Test
    void handleDeviceNotFoundException() throws Exception {
        Mockito.doThrow(DeviceNotFoundException.class)
                .when(deviceService).updateDevice(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(DeviceCreationRequest.class));
        var request = MockMvcRequestBuilders
                .put("/api/v1/devices/0d75f424-0ee4-48f8-83cd-c2067ab0c9bb")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name",
                          "brand": "Device Brand",
                          "state": "available"
                        }""");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.DEVICE_NOT_FOUND.getValue()));
    }

    @Test
    void handleDeviceInUseException() throws Exception {
        Mockito.doThrow(DeviceInUseException.class)
                .when(deviceService).updateDevice(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(DeviceCreationRequest.class));
        var request = MockMvcRequestBuilders
                .put("/api/v1/devices/0d75f424-0ee4-48f8-83cd-c2067ab0c9bb")
                .contentType(DeviceController.APPLICATION_DEVICE_REQUEST_V_1_JSON)
                .content("""
                        {
                          "name": "Device Name",
                          "brand": "Device Brand",
                          "state": "available"
                        }""");

        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ApiErrorCode.DEVICE_IN_USE.getValue()));
    }
}