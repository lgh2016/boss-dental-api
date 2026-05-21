package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response de appointments.
 */
@Getter
@Setter
public class AppointmentResponse {

    /**
     * ID de la cita.
     */
    private Long id;

    /**
     * ID del paciente.
     */
    private Long patientId;

    /**
     * Nombre completo del paciente.
     */
    private String patientName;

    /**
     * ID del dentist.
     */
    private Long dentistId;

    /**
     * Nombre completo del dentist.
     */
    private String dentistName;

    /**
     * Código del status actual.
     */
    private String statusCode;

    /**
     * Fecha de la cita.
     */
    private LocalDate appointmentDate;

    /**
     * Hora inicial de la cita.
     */
    private LocalTime startTime;

    /**
     * Hora final de la cita.
     */
    private LocalTime endTime;

    /**
     * Motivo principal de la cita.
     */
    private String reason;

    /**
     * Notas internas de la cita.
     */
    private String notes;

}