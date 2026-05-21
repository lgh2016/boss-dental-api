package mx.com.bossdental.api.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Request para actualizar la hora final
 * de una cita bloqueada.
 */
@Getter
@Setter
public class UpdateAppointmentEndTimeRequest {

    /**
     * Nueva hora final.
     */
    @NotNull
    private LocalTime endTime;

}