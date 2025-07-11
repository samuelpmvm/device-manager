package smarcos.implementation.services;

import com.model.device.DeviceCreationRequest;
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

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        LOGGER.info("DeviceService initialized with DeviceRepository");
    }

    public Device createDevice(DeviceCreationRequest deviceDto) {
        var device = DeviceMapper.toEntity(deviceDto);
        device.setCreationTime(OffsetDateTime.now());
        LOGGER.info("Creating device: {}", device);
        return deviceRepository.save(device);
    }

    @Transactional
    public Device updateDevice(UUID id, DeviceCreationRequest deviceDto) {
        LOGGER.info("Updating device with ID: {}", id);
        var existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with ID: " + id));

        if (existingDevice.isInUse()) {
            LOGGER.error("Attempted to update a device that is currently in use: {}", existingDevice);
            throw new DeviceInUseException("Cannot update a device that is currently in use.");
        }
        existingDevice.setName(deviceDto.getName());
        existingDevice.setBrand(deviceDto.getBrand());
        existingDevice.setState(deviceDto.getState());

        LOGGER.info("Updated device: {}", existingDevice);
        return deviceRepository.save(existingDevice);
    }

}
