package mx.com.bossdental.api.appointments.constants;

/**
 * Códigos técnicos del catálogo appointment_statuses.
 */
public final class AppointmentStatusCode {

    private AppointmentStatusCode() {}

    /**
     * Horario apartado temporalmente.
     */
    public static final String LOCKED = "LOCKED";

    /**
     * Cita confirmada.
     */
    public static final String CONFIRMED = "CONFIRMED";

    /**
     * Cita liberada por cancelación
     * o expiración de lock.
     */
    public static final String RELEASED = "RELEASED";

    /**
     * Cita cancelada manualmente.
     */
    public static final String CANCELLED = "CANCELLED";

    /**
     * Paciente atendido.
     */
    public static final String COMPLETED = "COMPLETED";

    /**
     * Paciente no asistió.
     */
    public static final String NO_SHOW = "NO_SHOW";

    /**
     * Paciente está en consulta
     */
    public static final String IN_PROGRESS = "IN_PROGRESS";

}