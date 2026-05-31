package mx.com.bossdental.api.dashboard.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Respuesta con la información resumida de una cita para la agenda del día.
 */
@Getter
@Builder
public class TodayAppointmentResponse {

    /**
     * Identificador de la cita.
     */
    private Long appointmentId;

    /**
     * Hora de inicio de la cita en formato HH:mm.
     */
    private String time;

    /**
     * Nombre completo del paciente.
     */
    private String patientName;

    /**
     * Motivo de la cita.
     */
    private String reason;

    /**
     * Nombre completo del doctor asignado.
     */
    private String doctorName;

    /**
     * Nombre de la sucursal.
     */
    private String branchName;

    /**
     * Código técnico del estatus.
     */
    private String statusCode;

    /**
     * Nombre visible del estatus.
     */
    private String statusName;
}