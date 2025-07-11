package smarcos.implementation.exceptions;

import java.io.Serial;

public class DeviceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9170757152647276639L;

    public DeviceNotFoundException(String message) {
        super(message);
    }
}
