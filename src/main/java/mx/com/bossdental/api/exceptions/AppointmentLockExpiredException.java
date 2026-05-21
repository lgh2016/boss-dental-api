package mx.com.bossdental.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class AppointmentLockExpiredException
        extends RuntimeException {

    public AppointmentLockExpiredException(
            String message
    ) {
        super(message);
    }
}