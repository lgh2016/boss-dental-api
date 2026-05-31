package mx.com.bossdental.api.patients.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Respuesta utilizada para pintar el panel lateral
 * de detalle del paciente.
 */
@Getter
@Setter
@Builder
public class PatientDetailResponse {

    /**
     * Identificador único del paciente.
     */
    private Long id;

    /**
     * Número de expediente del paciente.
     */
    private String expedientNumber;

    /**
     * Nombre completo del paciente.
     */
    private String fullName;

    /**
     * Género del paciente.
     */
    private String gender;

    /**
     * Edad calculada del paciente.
     */
    private Integer age;

    /**
     * Correo electrónico del paciente.
     */
    private String email;

    /**
     * Teléfono del paciente.
     */
    private String phone;

    /**
     * Sucursal asociada al paciente.
     */
    private String location;

    /**
     * Doctor asociado a la próxima cita del paciente.
     */
    private String doctorName;

    /**
     * URL de la imagen de perfil del paciente.
     */
    private String avatarUrl;

    /**
     * Iniciales del paciente para avatar genérico.
     */
    private String initials;

    /**
     * Saldo pendiente del paciente.
     */
    private BigDecimal balance;

    /**
     * Total pagado por el paciente.
     */
    private BigDecimal paidAmount;

    /**
     * Total presupuestado del paciente.
     */
    private BigDecimal totalBudgeted;

    /**
     * Última cita anterior a la fecha y hora actual.
     */
    private AppointmentSummaryResponse previousAppointment;

    /**
     * Próxima cita desde la fecha y hora actual.
     */
    private AppointmentSummaryResponse nextAppointment;
}