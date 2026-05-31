package mx.com.bossdental.api.dashboard.mapper;

import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentResponse;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentsCountResponse;
import mx.com.bossdental.api.dashboard.dto.response.TodayAppointmentsPageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mapper para respuestas del dashboard.
 */
@Mapper(componentModel = "spring")
public interface DashboardMapper {

    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Construye la respuesta del contador de citas del día actual.
     *
     * @param total total de citas activas encontradas
     * @return respuesta del contador
     */
    default TodayAppointmentsCountResponse toTodayAppointmentsCountResponse(Long total) {
        return TodayAppointmentsCountResponse.builder()
                .total(total)
                .build();
    }

    /**
     * Convierte una cita en respuesta resumida para la agenda del día.
     *
     * @param appointment cita encontrada
     * @return respuesta de cita para dashboard
     */
    default TodayAppointmentResponse toTodayAppointmentResponse(Appointment appointment) {
        return TodayAppointmentResponse.builder()
                .appointmentId(appointment.getId())
                .time(appointment.getStartTime() != null
                        ? appointment.getStartTime().format(TIME_FORMATTER)
                        : null)
                .patientName(appointment.getPatient() != null
                        ? appointment.getPatient().getName() + " " + appointment.getPatient().getLastName()
                        : null)
                .reason(appointment.getReason())
                .doctorName(appointment.getDentist() != null
                        ? appointment.getDentist().getName() + " " + appointment.getDentist().getLastName()
                        : null)
                .branchName(appointment.getBranch() != null
                        ? appointment.getBranch().getName()
                        : null)
                .statusCode(appointment.getStatus() != null
                        ? appointment.getStatus().getCode()
                        : null)
                .statusName(appointment.getStatus() != null
                        ? appointment.getStatus().getName()
                        : null)
                .build();
    }

    /**
     * Convierte una página de citas en respuesta paginada para dashboard.
     *
     * @param appointments página de citas
     * @return respuesta paginada
     */
    default TodayAppointmentsPageResponse toTodayAppointmentsPageResponse(Page<Appointment> appointments) {
        List<TodayAppointmentResponse> content = appointments.getContent()
                .stream()
                .map(this::toTodayAppointmentResponse)
                .toList();

        return TodayAppointmentsPageResponse.builder()
                .content(content)
                .page(appointments.getNumber())
                .size(appointments.getSize())
                .totalElements(appointments.getTotalElements())
                .totalPages(appointments.getTotalPages())
                .build();
    }
}