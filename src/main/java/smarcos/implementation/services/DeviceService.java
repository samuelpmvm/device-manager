package smarcos.implementation.services;

import com.model.device.DeviceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smarcos.implementation.entities.Device;
import smarcos.implementation.mapper.DeviceMapper;
import smarcos.implementation.repository.DeviceRepository;

import java.time.OffsetDateTime;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        LOGGER.info("DeviceService initialized with DeviceRepository");
    }

    public Device createDevice(DeviceDto deviceDto) {
        var device = DeviceMapper.toEntity(deviceDto);
        device.setCreationTime(OffsetDateTime.now());
        LOGGER.info("Creating device: {}", device);
        return deviceRepository.save(device);
    }


}
