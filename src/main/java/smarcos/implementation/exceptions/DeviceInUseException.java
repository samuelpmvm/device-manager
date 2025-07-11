package smarcos.implementation.exceptions;

import java.io.Serial;

public class DeviceInUseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4090887387119253603L;

    public DeviceInUseException(String message) {
        super(message);
    }
}
