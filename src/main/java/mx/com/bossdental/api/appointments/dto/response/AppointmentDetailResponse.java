package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Response de detalle de cita.
 */
@Getter
@Setter
public class AppointmentDetailResponse {

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
     * Motivo de consulta.
     */
    private String reason;

    /**
     * Notas internas.
     */
    private String notes;

    /**
     * Código de status.
     */
    private String statusCode;

    /**
     * Nombre del status.
     */
    private String statusName;

    /**
     * Color del status para frontend.
     */
    private String statusColor;

    /**
     * ID del paciente.
     */
    private Long patientId;

    /**
     * Nombre completo del paciente.
     */
    private String patientName;

    /**
     * Teléfono del paciente.
     */
    private String patientPhone;

    /**
     * Email del paciente.
     */
    private String patientEmail;

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
     * Fecha de confirmación.
     */
    private LocalDateTime confirmedAt;

    /**
     * Fecha de cancelación.
     */
    private LocalDateTime cancelledAt;
}