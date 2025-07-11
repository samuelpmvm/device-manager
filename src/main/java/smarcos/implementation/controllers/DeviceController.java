package smarcos.implementation.controllers;

import com.api.device.DeviceControllerApi;
import com.model.device.DeviceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.services.DeviceService;

@RestController
@RequestMapping("/api/v1")
public class DeviceController implements DeviceControllerApi {
    public static final String APPLICATION_DEVICE_REQUEST_V_1_JSON= "application/device-request-v1+json";

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<DeviceDto> createDevice(DeviceDto deviceDto) {
        var device = deviceService.createDevice(deviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceMapper.toDto(device));
    }
}