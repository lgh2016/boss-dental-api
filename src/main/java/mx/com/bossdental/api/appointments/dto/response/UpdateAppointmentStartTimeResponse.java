package mx.com.bossdental.api.appointments.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record UpdateAppointmentStartTimeResponse(
        Long appointmentId,
        LocalTime startTime,
        LocalDateTime lockedUntil
) {
}
