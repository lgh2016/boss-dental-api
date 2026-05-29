package mx.com.bossdental.api.appointments.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CleanupExpiredLocksResponse {

    /**
     * Cantidad de locks expirados eliminados.
     */
    private Integer deletedCount;

    /**
     * Fecha y hora de ejecución.
     */
    private LocalDateTime executedAt;

    /**
     * Mensaje informativo.
     */
    private String message;
}