package mx.com.bossdental.api.appointments.constants;

/**
 * Códigos técnicos del catálogo appointment_statuses.
 */
public final class AppointmentStatusCode {

    private AppointmentStatusCode() {}

    public static final String LOCKED = "LOCKED";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String RELEASED = "RELEASED";
    public static final String CANCELLED = "CANCELLED";
    public static final String COMPLETED = "COMPLETED";
    public static final String NO_SHOW = "NO_SHOW";
}