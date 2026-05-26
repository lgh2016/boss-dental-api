package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response de actualización de fecha
 * para una cita bloqueada.
 */
@Getter
@Setter
public class UpdateAppointmentDateResponse {

    /**
     * ID de la cita.
     */
    private Long appointmentId;

    /**
     * Nueva fecha de la cita.
     */
    private LocalDate appointmentDate;

    /**
     * Fecha de expiración del lock.
     */
    private LocalDateTime lockedUntil;

}