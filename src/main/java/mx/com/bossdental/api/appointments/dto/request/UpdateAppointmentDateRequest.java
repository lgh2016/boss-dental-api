package mx.com.bossdental.api.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request para actualizar la fecha
 * de una cita bloqueada.
 */
@Getter
@Setter
public class UpdateAppointmentDateRequest {

    /**
     * Nueva fecha seleccionada para la cita.
     */
    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDate appointmentDate;

}