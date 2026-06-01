package mx.com.bossdental.api.activity.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.activity.entity.ActivityLog;
import mx.com.bossdental.api.activity.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de registrar
 * actividades del sistema.
 */
@Service
@RequiredArgsConstructor
public class RegisterActivityService {

    private final ActivityLogRepository activityLogRepository;

    /**
     * Registra una actividad del sistema.
     *
     * @param actorType tipo de actor.
     * @param actorUserId ID del usuario actor.
     * @param actionCode código de acción.
     * @param module módulo origen.
     * @param entityType tipo de entidad afectada.
     * @param entityId ID de la entidad afectada.
     * @param title título de actividad.
     * @param description descripción de actividad.
     */
    @Transactional
    public void registerActivity(
            String actorType,
            Long actorUserId,
            Long patientId,
            String actionCode,
            String module,
            String entityType,
            Long entityId,
            String title,
            String description
    ) {

        /*
         * Construir actividad.
         */
        ActivityLog activityLog = new ActivityLog();

        activityLog.setActorType(actorType);
        activityLog.setActorUserId(actorUserId);
        activityLog.setPatientId(patientId);
        activityLog.setActionCode(actionCode);
        activityLog.setModule(module);
        activityLog.setEntityType(entityType);
        activityLog.setEntityId(entityId);
        activityLog.setTitle(title);
        activityLog.setDescription(description);

        /*
         * Guardar actividad.
         */
        activityLogRepository.save(activityLog);
    }
}