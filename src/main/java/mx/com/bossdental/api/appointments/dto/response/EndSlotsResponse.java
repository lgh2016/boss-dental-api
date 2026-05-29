    package mx.com.bossdental.api.appointments.dto.response;

    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalTime;
    import java.util.List;

    /**
     * Response de horas fin disponibles
     * para una cita bloqueada existente.
     */
    @Getter
    @Setter
    public class EndSlotsResponse {

        /**
         * ID de la cita bloqueada.
         */
        private Long appointmentId;

        /**
         * Hora inicio usada para calcular las horas fin.
         */
        private LocalTime startTime;

        /**
         * Lista de horas fin disponibles.
         */
        private List<LocalTime> endSlots;

    }