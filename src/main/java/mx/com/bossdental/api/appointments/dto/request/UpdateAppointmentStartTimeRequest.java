package mx.com.bossdental.api.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Request para actualizar la hora de inicio de una cita bloqueada.
 */
@Getter
@Setter
public class UpdateAppointmentStartTimeRequest {

    /**
     * Nueva hora de inicio seleccionada por el usuario.
     */
    @NotNull(message = "La hora inicio es obligatoria")
    private LocalTime startTime;
}