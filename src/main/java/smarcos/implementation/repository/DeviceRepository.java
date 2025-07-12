package smarcos.implementation.repository;

import com.model.device.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smarcos.implementation.entities.Device;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
    List<Device> findByBrandAndState(String brand, DeviceState deviceState);
}
