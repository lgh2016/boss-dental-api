package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentDayScheduleResponse {

    /**
     * ID de la cita.
     */
    private Long id;

    /**
     * Fecha de la cita.
     */
    private LocalDate appointmentDate;

    /**
     * Hora inicio.
     */
    private LocalTime startTime;

    /**
     * Hora fin.
     */
    private LocalTime endTime;

    /**
     * Código de status.
     */
    private String statusCode;

    /**
     * Nombre del status.
     */
    private String statusName;

    /**
     * ID del paciente.
     */
    private Long patientId;

    /**
     * Nombre completo del paciente.
     */
    private String patientName;

    /**
     * ID del doctor.
     */
    private Long dentistId;

    /**
     * Nombre completo del doctor.
     */
    private String dentistName;

    /**
     * ID de sucursal.
     */
    private Long branchId;

    /**
     * Nombre de sucursal.
     */
    private String branchName;

    /**
     * Motivo de consulta.
     */
    private String reason;
}
