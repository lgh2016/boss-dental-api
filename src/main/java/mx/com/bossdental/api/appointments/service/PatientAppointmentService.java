package mx.com.bossdental.api.appointments.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.constants.AppointmentStatusCode;

import mx.com.bossdental.api.appointments.dto.response.PatientAppointmentResponse;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.mapper.PatientAppointmentMapper;
import mx.com.bossdental.api.appointments.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servicio para consultar citas
 * asociadas a un paciente.
 */
@Service
@RequiredArgsConstructor
public class PatientAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientAppointmentMapper patientAppointmentMapper;

    /**
     * Consulta las citas activas de un paciente.
     *
     * @param patientId ID del paciente.
     * @return lista de citas del paciente.
     */
    @Transactional(readOnly = true)
    public List<PatientAppointmentResponse> findAppointmentsByPatientId(Long patientId) {

        return appointmentRepository
                .findByPatientIdAndActiveTrueOrderByAppointmentDateDescStartTimeDesc(patientId)
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    /**
     * Construye la respuesta de una cita
     * aplicando reglas de acciones disponibles.
     *
     * @param appointment cita encontrada.
     * @return DTO de respuesta.
     */
    private PatientAppointmentResponse buildResponse(Appointment appointment) {

        PatientAppointmentResponse response = patientAppointmentMapper.toResponse(appointment);

        response.setStatusColor(resolveStatusColor(getStatusCode(appointment)));
        response.setCanCancel(canCancel(appointment));
        response.setCanReschedule(canReschedule(appointment));

        return response;
    }

    /**
     * Valida si la cita puede cancelarse.
     *
     * @param appointment cita a validar.
     * @return true si puede cancelarse.
     */
    private boolean canCancel(Appointment appointment) {

        String statusCode = getStatusCode(appointment);

        return AppointmentStatusCode.CONFIRMED.equals(statusCode)
                && !isPastOrStartedAppointment(appointment);
    }

    /**
     * Valida si la cita puede reagendarse.
     *
     * @param appointment cita a validar.
     * @return true si puede reagendarse.
     */
    private boolean canReschedule(Appointment appointment) {

        String statusCode = getStatusCode(appointment);

        return AppointmentStatusCode.CONFIRMED.equals(statusCode)
                && !isPastOrStartedAppointment(appointment);
    }

    /**
     * Determina si la cita ya inició
     * o pertenece a una fecha pasada.
     *
     * @param appointment cita a validar.
     * @return true si ya no debe modificarse.
     */
    private boolean isPastOrStartedAppointment(Appointment appointment) {

        if (appointment.getAppointmentDate() == null || appointment.getStartTime() == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return appointment.getAppointmentDate().isBefore(today)
                || appointment.getAppointmentDate().isEqual(today)
                && !appointment.getStartTime().isAfter(now);
    }

    /**
     * Obtiene el código de estado de la cita.
     *
     * @param appointment cita.
     * @return código de estado.
     */
    private String getStatusCode(Appointment appointment) {

        if (appointment == null || appointment.getStatus() == null) {
            return null;
        }

        return appointment.getStatus().getCode();
    }

    /**
     * Resuelve el color visible del estado.
     *
     * @param statusCode código técnico del estado.
     * @return color para frontend.
     */
    private String resolveStatusColor(String statusCode) {

        if (AppointmentStatusCode.CONFIRMED.equals(statusCode)) {
            return "GREEN";
        }

        if (AppointmentStatusCode.IN_PROGRESS.equals(statusCode)) {
            return "BLUE";
        }

        if (AppointmentStatusCode.COMPLETED.equals(statusCode)) {
            return "GRAY";
        }

        if (AppointmentStatusCode.CANCELLED.equals(statusCode)) {
            return "RED";
        }

        if (AppointmentStatusCode.RELEASED.equals(statusCode)) {
            return "GRAY";
        }

        if (AppointmentStatusCode.LOCKED.equals(statusCode)) {
            return "YELLOW";
        }

        return "GRAY";
    }
}