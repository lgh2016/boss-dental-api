package mx.com.bossdental.api.activity.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para actividades
 * relacionadas con un paciente.
 */
@Getter
@Setter
public class PatientActivityLogResponse {

    /**
     * ID de la actividad.
     */
    private Long id;

    /**
     * Código técnico de la acción.
     */
    private String actionCode;

    /**
     * Módulo origen.
     */
    private String module;

    /**
     * Tipo de entidad relacionada.
     */
    private String entityType;

    /**
     * ID de la entidad relacionada.
     */
    private Long entityId;

    /**
     * Título visible.
     */
    private String title;

    /**
     * Descripción visible.
     */
    private String description;

    /**
     * Tipo de actor.
     */
    private String actorType;

    /**
     * ID del usuario actor.
     */
    private Long actorUserId;

    /**
     * Nombre visible del usuario actor.
     */
    private String actorName;

    /**
     * Rol visible del usuario actor.
     */
    private String actorRole;

    /**
     * Información adicional de la actividad.
     */
    private String metadata;

    /**
     * Fecha de creación del registro.
     */
    private LocalDateTime createdAt;
}