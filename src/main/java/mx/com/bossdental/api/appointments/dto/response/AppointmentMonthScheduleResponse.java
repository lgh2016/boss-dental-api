package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentMonthScheduleResponse {

    /**
     * Fecha correspondiente.
     */
    private LocalDate date;

    /**
     * Total de citas.
     */
    private Integer totalAppointments;

    /**
     * Total de citas confirmadas.
     */
    private Integer confirmedCount;

    /**
     * Total de citas completadas.
     */
    private Integer completedCount;

    /**
     * Total de citas canceladas.
     */
    private Integer cancelledCount;

    /**
     * Nivel de carga.
     *
     * VACIO
     * BAJA
     * MEDIA
     * ALTA
     */
    private String loadLevel;
}