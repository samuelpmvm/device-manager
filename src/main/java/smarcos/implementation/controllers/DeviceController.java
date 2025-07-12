package smarcos.implementation.controllers;

import com.api.device.DeviceControllerApi;
import com.model.device.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.services.DeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DeviceController implements DeviceControllerApi {
    public static final String APPLICATION_DEVICE_REQUEST_V_1_JSON= "application/device-request-v1+json";

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<DeviceResponse> createDevice(DeviceCreationRequest deviceCreationRequest) {
        var device = deviceService.createDevice(deviceCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceMapper.toDeviceResponse(device));
    }

    @Override
    public ResponseEntity<DeviceResponse> updateDevice(UUID id, DeviceCreationRequest deviceCreationRequest) {
        var device = deviceService.updateDevice(id, deviceCreationRequest);
        return ResponseEntity.ok(DeviceMapper.toDeviceResponse(device));
    }

    @Override
    public ResponseEntity<DeviceResponse> partiallyUpdateDevice(UUID id, DevicePartiallyUpdateRequest devicePartiallyUpdateRequest) throws Exception {
        var device = deviceService.partiallyUpdateDevice(id, devicePartiallyUpdateRequest);
        return ResponseEntity.ok(DeviceMapper.toDeviceResponse(device));
    }

    @Override
    public ResponseEntity<DeviceResponse> getDeviceById(UUID id) {
        var device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(DeviceMapper.toDeviceResponse(device));
    }

    @Override
    public ResponseEntity<Void> deleteDevice(UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<DevicesResponse> findDevices(DeviceState state, String brand) {
        return ResponseEntity.ok(DeviceMapper.toDevicesResponse(deviceService.findDevices(state, brand)));
    }
}