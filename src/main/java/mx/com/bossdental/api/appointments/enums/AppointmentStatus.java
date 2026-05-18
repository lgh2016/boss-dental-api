package mx.com.bossdental.api.appointments.enums;

/**
 * Estados de appointments (citas)
 */
public enum AppointmentStatus {

    /**
     * Horario apartado temporalmente.
     */
    LOCKED,

    /**
     * Cita confirmada.
     */
    CONFIRMED,

    /**
     * Cita liberada por cancelación
     * o expiración de lock.
     */
    RELEASED,

    /**
     * Cita cancelada manualmente.
     */
    CANCELLED,

    /**
     * Paciente atendido.
     */
    COMPLETED,

    /**
     * Paciente no asistió.
     */
    NO_SHOW
}