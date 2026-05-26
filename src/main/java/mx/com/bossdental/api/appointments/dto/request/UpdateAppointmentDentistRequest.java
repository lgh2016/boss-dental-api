package mx.com.bossdental.api.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request para actualizar doctor
 * de una cita bloqueada.
 */
@Getter
@Setter
public class UpdateAppointmentDentistRequest {

    /**
     * Nuevo doctor seleccionado.
     */
    @NotNull(message = "El doctor es obligatorio")
    private Long dentistId;
}
