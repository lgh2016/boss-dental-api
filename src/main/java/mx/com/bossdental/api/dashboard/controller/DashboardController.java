package mx.com.bossdental.api.dashboard.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentsCountResponse;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentsPageResponse;
import mx.com.bossdental.api.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para métricas del dashboard.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Obtiene el total de citas activas programadas para hoy.
     *
     * @return total de citas del día actual
     */
    @GetMapping("/appointments/today/count")
    public TodayAppointmentsCountResponse countTodayAppointments() {
        return dashboardService.countTodayAppointments();
    }

    /**
     * Obtiene la agenda de citas activas programadas para hoy.
     *
     * @param page página solicitada
     * @param size tamaño de página
     * @return citas del día paginadas
     */
    @GetMapping("/appointments/today")
    public TodayAppointmentsPageResponse findTodayAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return dashboardService.findTodayAppointments(page, size);
    }
}