package mx.com.bossdental.api.dashboard.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.repository.AppointmentRepository;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentsCountResponse;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentsPageResponse;
import mx.com.bossdental.api.dashboard.mapper.DashboardMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Servicio para consultas del dashboard.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final DashboardMapper dashboardMapper;

    /**
     * Obtiene el total de citas activas programadas para el día actual.
     *
     * @return respuesta con el total de citas de hoy
     */
    public TodayAppointmentsCountResponse countTodayAppointments() {
        Long total = appointmentRepository.countTodayAppointments(LocalDate.now());
        return dashboardMapper.toTodayAppointmentsCountResponse(total);
    }

    /**
     * Obtiene la agenda de citas activas del día actual de forma paginada.
     *
     * @param page página solicitada
     * @param size tamaño de página
     * @return respuesta paginada con citas del día
     */
    public TodayAppointmentsPageResponse findTodayAppointments(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentRepository.findTodayAppointments(LocalDate.now(), pageRequest);

        return dashboardMapper.toTodayAppointmentsPageResponse(appointments);
    }
}