package mx.com.bossdental.api.appointments.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request para confirmar una cita previamente bloqueada.
 */
@Getter
@Setter
public class ConfirmAppointmentRequest {

    /**
     * ID del paciente asignado a la cita.
     */
    @NotNull
    private Long patientId;

    /**
     * Motivo principal de la cita.
     */
    private String reason;

    /**
     * Notas internas de la cita.
     */
    private String notes;

}