package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response de actualización de doctor
 * para una cita bloqueada.
 */
@Getter
@Setter
public class UpdateAppointmentDentistResponse {

    /**
     * ID de la cita.
     */
    private Long appointmentId;

    /**
     * ID del doctor actualizado.
     */
    private Long dentistId;

    /**
     * Fecha de expiración del lock.
     */
    private LocalDateTime lockedUntil;

}