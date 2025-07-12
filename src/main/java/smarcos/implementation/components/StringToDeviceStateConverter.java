package smarcos.implementation.components;

import com.model.device.DeviceState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDeviceStateConverter implements Converter<String, DeviceState> {
    @Override
    public DeviceState convert(String source) {
        return DeviceState.fromValue(source.toLowerCase());
    }
}
