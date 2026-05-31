package mx.com.bossdental.api.dashboard.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Respuesta paginada para la agenda de citas del día.
 */
@Getter
@Builder
public class TodayAppointmentsPageResponse {

    /**
     * Lista de citas de la página solicitada.
     */
    private List<TodayAppointmentResponse> content;

    /**
     * Página actual.
     */
    private int page;

    /**
     * Tamaño solicitado.
     */
    private int size;

    /**
     * Total de elementos encontrados.
     */
    private long totalElements;

    /**
     * Total de páginas disponibles.
     */
    private int totalPages;
}