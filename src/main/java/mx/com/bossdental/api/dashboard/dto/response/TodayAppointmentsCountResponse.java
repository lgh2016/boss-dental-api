package mx.com.bossdental.api.dashboard.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Respuesta para el contador de citas del día actual.
 */
@Getter
@Builder
public class TodayAppointmentsCountResponse {

    /**
     * Total de citas activas programadas para hoy.
     */
    private Long total;
}