package mx.com.bossdental.api.activity.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response de actividad del sistema.
 */
@Getter
@Setter
public class ActivityLogResponse {

    /**
     * ID del registro.
     */
    private Long id;

    /**
     * Tipo de actor.
     */
    private String actorType;

    /**
     * ID del usuario que ejecutó la acción.
     */
    private Long actorUserId;

    /**
     * Código de acción.
     */
    private String actionCode;

    /**
     * Módulo origen.
     */
    private String module;

    /**
     * Tipo de entidad afectada.
     */
    private String entityType;

    /**
     * ID de la entidad afectada.
     */
    private Long entityId;

    /**
     * Título de la actividad.
     */
    private String title;

    /**
     * Descripción de la actividad.
     */
    private String description;

    /**
     * Fecha de creación.
     */
    private LocalDateTime createdAt;
}