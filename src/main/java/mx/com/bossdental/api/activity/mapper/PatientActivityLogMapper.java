package mx.com.bossdental.api.activity.mapper;

import mx.com.bossdental.api.activity.dto.response.PatientActivityLogResponse;
import mx.com.bossdental.api.activity.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para transformar actividades
 * a respuestas de historial.
 */
@Mapper(componentModel = "spring")
public interface PatientActivityLogMapper {

    /**
     * Convierte una actividad a DTO.
     *
     * @param activityLog actividad.
     * @return respuesta.
     */
    @Mapping(target = "actorName", ignore = true)
    @Mapping(target = "actorRole", ignore = true)
    PatientActivityLogResponse toResponse(
            ActivityLog activityLog
    );
}