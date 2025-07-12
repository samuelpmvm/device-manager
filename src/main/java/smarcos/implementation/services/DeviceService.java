package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
import com.model.device.DevicePartiallyUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smarcos.implementation.entities.Device;
import smarcos.implementation.exceptions.DeviceInUseException;
import smarcos.implementation.exceptions.DeviceNotFoundException;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.repository.DeviceRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private static final String DEVICE_NOT_FOUND_WITH_ID = "Device not found with ID: ";

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        LOGGER.info("DeviceService initialized with DeviceRepository");
    }

    @Transactional
    public Device createDevice(DeviceCreationRequest deviceCreationRequest) {
        var device = DeviceMapper.toEntity(deviceCreationRequest);
        device.setCreationTime(OffsetDateTime.now());
        LOGGER.info("Creating device: {}", device);
        return deviceRepository.save(device);
    }

    @Transactional
    public Device updateDevice(UUID id, DeviceCreationRequest deviceCreationRequest) {
        LOGGER.info("Updating device with ID: {}", id);
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(DEVICE_NOT_FOUND_WITH_ID + id));

        if (existingDevice.isInUse()) {
            LOGGER.error("Attempted to update a device that is currently in use: {}", existingDevice);
            throw new DeviceInUseException("Cannot update a device that is currently in use.");
        }
        existingDevice.setName(deviceCreationRequest.getName());
        existingDevice.setBrand(deviceCreationRequest.getBrand());
        existingDevice.setState(deviceCreationRequest.getState());

        LOGGER.info("Updated device: {}", existingDevice);
        return deviceRepository.save(existingDevice);
    }

    @Transactional
    public Device partiallyUpdateDevice(UUID id, DevicePartiallyUpdateRequest devicePartiallyUpdateRequest) {
        LOGGER.info("Partially updating device with ID: {}", id);
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(DEVICE_NOT_FOUND_WITH_ID + id));

        if (existingDevice.isInUse() && isToUpdateNameOrBrand(devicePartiallyUpdateRequest)) {
            LOGGER.error("Attempted to partially update name or brand device that is currently in use: {}", existingDevice);
            throw new DeviceInUseException("Cannot update name or brand device that is currently in use.");
        }
        updateExistingDevice(existingDevice, devicePartiallyUpdateRequest);

        LOGGER.info("Partially updated device: {}", existingDevice);
        return deviceRepository.save(existingDevice);
    }

    @Transactional(readOnly = true)
    public Device getDeviceById(UUID id) {
        LOGGER.info("Fetch Device with ID: {}", id);
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(DEVICE_NOT_FOUND_WITH_ID + id));
    }

    private boolean isToUpdateNameOrBrand(DevicePartiallyUpdateRequest devicePartiallyUpdateRequest) {
        return devicePartiallyUpdateRequest.getBrand() != null || devicePartiallyUpdateRequest.getName() != null;
    }

    private void updateExistingDevice(Device device, DevicePartiallyUpdateRequest devicePartiallyUpdateRequest){
        if(devicePartiallyUpdateRequest.getName() != null) device.setName(devicePartiallyUpdateRequest.getName());
        if(devicePartiallyUpdateRequest.getBrand() != null) device.setBrand(devicePartiallyUpdateRequest.getBrand());
        if(devicePartiallyUpdateRequest.getState() != null) device.setState(devicePartiallyUpdateRequest.getState());
    }

}
