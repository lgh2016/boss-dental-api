package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO de respuesta para mostrar
 * las citas asociadas a un paciente.
 */
@Getter
@Setter
public class PatientAppointmentResponse {

    /**
     * ID de la cita.
     */
    private Long id;

    /**
     * Fecha de la cita.
     */
    private LocalDate appointmentDate;

    /**
     * Hora de inicio de la cita.
     */
    private LocalTime startTime;

    /**
     * Hora de fin de la cita.
     */
    private LocalTime endTime;

    /**
     * Motivo o tratamiento registrado.
     */
    private String reason;

    /**
     * Nombre completo del doctor.
     */
    private String doctorName;

    /**
     * Nombre de la sucursal.
     */
    private String branchName;

    /**
     * Código técnico del estado.
     */
    private String statusCode;

    /**
     * Nombre visible del estado.
     */
    private String statusName;

    /**
     * Color del estado para frontend.
     */
    private String statusColor;

    /**
     * Indica si la cita puede cancelarse.
     */
    private Boolean canCancel;

    /**
     * Indica si la cita puede reagendarse.
     */
    private Boolean canReschedule;
}
