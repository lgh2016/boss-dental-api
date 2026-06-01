package mx.com.bossdental.api.activity.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.activity.dto.response.PatientActivityLogResponse;
import mx.com.bossdental.api.activity.entity.ActivityLog;
import mx.com.bossdental.api.activity.mapper.PatientActivityLogMapper;
import mx.com.bossdental.api.activity.repository.ActivityLogRepository;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.users.entity.User;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para consultar actividades
 * registradas en la bitácora del sistema.
 */
@Service
@RequiredArgsConstructor
public class PatientActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final PatientActivityLogMapper patientActivityLogMapper;
    private final UserRepository userRepository;

    /**
     * Consulta la actividad reciente general del sistema.
     *
     * @param page página actual.
     * @param size tamaño de página.
     * @return actividades recientes paginadas.
     */
    @Transactional(readOnly = true)
    public PageResponse<PatientActivityLogResponse> findRecentActivities(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ActivityLog> result = activityLogRepository
                .findByActiveTrueOrderByCreatedAtDesc(pageable);

        return buildPageResponse(result);
    }

    /**
     * Consulta el historial de actividades de un paciente.
     *
     * @param patientId ID del paciente.
     * @param page página actual.
     * @param size tamaño de página.
     * @return actividades del paciente paginadas.
     */
    @Transactional(readOnly = true)
    public PageResponse<PatientActivityLogResponse> findByPatientId(
            Long patientId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ActivityLog> result = activityLogRepository
                .findByPatientIdAndActiveTrueOrderByCreatedAtDesc(
                        patientId,
                        pageable
                );

        return buildPageResponse(result);
    }

    /**
     * Construye la respuesta paginada.
     *
     * @param result página de actividades.
     * @return respuesta paginada.
     */
    private PageResponse<PatientActivityLogResponse> buildPageResponse(
            Page<ActivityLog> result
    ) {

        List<PatientActivityLogResponse> content = result.getContent()
                .stream()
                .map(this::buildResponse)
                .toList();

        return new PageResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    /**
     * Construye la respuesta individual de actividad.
     *
     * @param activityLog actividad encontrada.
     * @return DTO de actividad.
     */
    private PatientActivityLogResponse buildResponse(ActivityLog activityLog) {

        PatientActivityLogResponse response =
                patientActivityLogMapper.toResponse(activityLog);

        if (activityLog.getActorUserId() != null) {
            userRepository.findById(activityLog.getActorUserId())
                    .ifPresent(user -> fillActorData(response, user));
        }

        return response;
    }

    /**
     * Llena los datos visibles del usuario actor.
     *
     * @param response DTO de respuesta.
     * @param user usuario actor.
     */
    private void fillActorData(
            PatientActivityLogResponse response,
            User user
    ) {

        response.setActorName(
                user.getName() + " " + user.getLastName()
        );

        if (user.getRole() != null) {
            response.setActorRole(user.getRole().getName());
        }
    }
}