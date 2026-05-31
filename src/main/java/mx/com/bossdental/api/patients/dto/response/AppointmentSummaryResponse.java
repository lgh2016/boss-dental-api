package mx.com.bossdental.api.patients.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Respuesta resumida de una cita utilizada
 * en paneles de consulta rápida del paciente.
 */
@Getter
@Setter
@Builder
public class AppointmentSummaryResponse {

    /**
     * Identificador único de la cita.
     */
    private Long id;

    /**
     * Fecha de la cita.
     */
    private LocalDate date;

    /**
     * Hora de inicio de la cita.
     */
    private LocalTime time;

    /**
     * Motivo de la cita.
     */
    private String reason;

    /**
     * Nombre completo del doctor asignado.
     */
    private String doctorName;

    /**
     * Código técnico del estatus de la cita.
     */
    private String status;
}