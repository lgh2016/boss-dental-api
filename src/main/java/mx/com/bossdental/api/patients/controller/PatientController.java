package mx.com.bossdental.api.patients.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.activity.dto.response.PatientActivityLogResponse;
import mx.com.bossdental.api.activity.service.PatientActivityLogService;
import mx.com.bossdental.api.appointments.dto.response.PatientAppointmentResponse;
import mx.com.bossdental.api.appointments.service.PatientAppointmentService;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.common.dto.Response;
import mx.com.bossdental.api.patients.dto.request.PatientCreateRequest;
import mx.com.bossdental.api.patients.dto.response.PatientDetailResponse;
import mx.com.bossdental.api.patients.dto.response.PatientListResponse;
import mx.com.bossdental.api.patients.dto.response.PatientResponse;

import mx.com.bossdental.api.patients.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientAppointmentService patientAppointmentService;
    private final PatientActivityLogService patientActivityLogService;

    @GetMapping
    public ResponseEntity<PageResponse<PatientListResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query
    ) {

        return ResponseEntity.ok(
                patientService.findAll(page, size, query)
        );
    }

    @PostMapping
    public PatientResponse createPatient(
            @Valid @RequestBody PatientCreateRequest request
    ) {
        return patientService.createPatient(request);
    }

    /**
     * Obtiene la información general del paciente necesaria
     * para pintar el panel lateral de detalle.
     *
     * Incluye información personal, datos de contacto,
     * doctor asignado, métricas financieras y resumen
     * de citas anterior y próxima.
     *
     * @param patientId identificador único del paciente
     * @return detalle completo del paciente
     */
    @GetMapping("/{patientId}/detail")
    public Response<PatientDetailResponse> getPatientDetail(
            @PathVariable Long patientId) {

        return Response.<PatientDetailResponse>builder()
                .data(patientService.getPatientDetail(patientId))
                .message("Detalle del paciente obtenido correctamente")
                .success(Boolean.TRUE)
                .build();
    }

    /**
     * Obtiene las citas de un paciente.
     *
     * @param patientId ID del paciente.
     * @return lista de citas del paciente.
     */
    @GetMapping("/{patientId}/appointments")
    public Response<List<PatientAppointmentResponse>> findAppointmentsByPatientId(
            @PathVariable Long patientId
    ) {
        return Response.<List<PatientAppointmentResponse>>builder()
                .data(patientAppointmentService.findAppointmentsByPatientId(patientId))
                .message("Citas del paciente obtenidas correctamente")
                .success(Boolean.TRUE)
                .build();
    }

    /**
     * Obtiene el historial de actividades de un paciente.
     *
     * @param patientId ID del paciente.
     * @param page página actual.
     * @param size tamaño de página.
     * @return historial del paciente.
     */
    @GetMapping("/{patientId}/activity-logs")
    public Response<PageResponse<PatientActivityLogResponse>> findActivityLogsByPatientId(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return Response.<PageResponse<PatientActivityLogResponse>>builder()
                .data(
                        patientActivityLogService.findByPatientId(
                                patientId,
                                page,
                                size
                        )
                )
                .message("Historial del paciente obtenido correctamente")
                .success(Boolean.TRUE)
                .build();
    }
}