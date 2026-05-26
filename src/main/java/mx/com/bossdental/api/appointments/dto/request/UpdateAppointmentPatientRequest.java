package mx.com.bossdental.api.appointments.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request para actualizar el paciente de una cita bloqueada.
 *
 * Este flujo aplica cuando ya existe un appointment en estado LOCKED
 * y el usuario cambia el paciente desde la pantalla de creación de cita.
 */
@Getter
@Setter
public class UpdateAppointmentPatientRequest {

    /**
     * Identificador del nuevo paciente seleccionado.
     */
    private Long patientId;
}