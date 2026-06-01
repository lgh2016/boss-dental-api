package mx.com.bossdental.api.activity.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.activity.dto.response.PatientActivityLogResponse;
import mx.com.bossdental.api.activity.service.PatientActivityLogService;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.common.dto.Response;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de actividad reciente del sistema.
 */
@RestController
@RequestMapping("/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final PatientActivityLogService patientActivityLogService;

    /**
     * Obtiene la actividad reciente general del sistema.
     *
     * @param page página actual.
     * @param size tamaño de página.
     * @return actividades recientes.
     */
    @GetMapping
    public Response<PageResponse<PatientActivityLogResponse>> findRecentActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return Response.<PageResponse<PatientActivityLogResponse>>builder()
                .data(patientActivityLogService.findRecentActivities(page, size))
                .message("Actividad reciente obtenida correctamente")
                .success(Boolean.TRUE)
                .build();
    }
}