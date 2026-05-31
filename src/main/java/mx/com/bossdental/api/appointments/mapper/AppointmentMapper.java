package mx.com.bossdental.api.appointments.mapper;

import mx.com.bossdental.api.appointments.constants.AppointmentStatusCode;
import mx.com.bossdental.api.appointments.dto.response.*;
import mx.com.bossdental.api.appointments.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper de appointments.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    /**
     * Convierte entidad Appointment a response.
     *
     * @param appointment entidad.
     * @return response.
     */
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "dentistId", source = "dentist.id")
    @Mapping(target = "dentistName", source = "dentist.name")
    @Mapping(target = "statusCode", source = "status.code")
    AppointmentResponse toResponse(
            Appointment appointment
    );

    /**
     * Convierte Appointment a response de actualización de hora inicio.
     *
     * @param appointment entidad de cita.
     * @return response de hora inicio actualizada.
     */
    @Mapping(target = "appointmentId", source = "id")
    UpdateAppointmentStartTimeResponse toUpdateStartTimeResponse(
            Appointment appointment
    );

    /**
     * Convierte Appointment a response de actualización de doctor.
     *
     * @param appointment entidad de cita.
     * @return response de doctor actualizado.
     */
    @Mapping(target = "appointmentId", source = "id")
    @Mapping(target = "dentistId", source = "dentist.id")
    UpdateAppointmentDentistResponse toUpdateDentistResponse(
            Appointment appointment
    );

    /**
     * Convierte Appointment a response
     * de actualización de fecha.
     *
     * @param appointment entidad.
     * @return response.
     */
    @Mapping(target = "appointmentId", source = "id")
    UpdateAppointmentDateResponse toUpdateDateResponse(
            Appointment appointment
    );

    /**
     * Convierte cita a response
     * de agenda diaria.
     *
     * @param appointment cita.
     * @return response de agenda diaria.
     */
    @Mapping(target = "statusCode", source = "status.code")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(buildPatientName(appointment))")
    @Mapping(target = "dentistId", source = "dentist.id")
    @Mapping(target = "dentistName", expression = "java(buildDentistName(appointment))")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    AppointmentDayScheduleResponse toDayScheduleResponse(
            Appointment appointment
    );

    /**
     * Convierte citas a responses
     * de agenda diaria.
     *
     * @param appointments citas.
     * @return responses de agenda diaria.
     */
    List<AppointmentDayScheduleResponse> toDayScheduleResponseList(
            List<Appointment> appointments
    );

    /**
     * Construye nombre completo del paciente.
     *
     * @param appointment cita.
     * @return nombre completo.
     */
    default String buildPatientName(
            Appointment appointment
    ) {

        if (appointment == null
                || appointment.getPatient() == null) {
            return null;
        }

        return appointment.getPatient().getName()
                + " "
                + appointment.getPatient().getLastName();
    }

    /**
     * Construye nombre completo del doctor.
     *
     * @param appointment cita.
     * @return nombre completo.
     */
    default String buildDentistName(
            Appointment appointment
    ) {

        if (appointment == null
                || appointment.getDentist() == null) {
            return null;
        }

        String prefix = "Dr.";

        if ("FEMALE".equals(
                appointment.getDentist().getGender()
        )) {

            prefix = "Dra.";
        }

        return prefix
                + " "
                + appointment.getDentist().getName()
                + " "
                + appointment.getDentist().getLastName();
    }

    /**
     * Convierte cita a detalle.
     *
     * @param appointment cita.
     * @return detalle de cita.
     */
    @Mapping(target = "statusCode", source = "status.code")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "statusColor", expression = "java(resolveStatusColor(appointment))")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(buildPatientName(appointment))")
    @Mapping(target = "patientPhone", source = "patient.phone")
    @Mapping(target = "patientEmail", source = "patient.email")
    @Mapping(target = "dentistId", source = "dentist.id")
    @Mapping(target = "dentistName", expression = "java(buildDentistName(appointment))")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    AppointmentDetailResponse toDetailResponse(
            Appointment appointment
    );

    /**
     * Resuelve color de status
     * para frontend.
     *
     * @param appointment cita.
     * @return color de status.
     */
    default String resolveStatusColor(
            Appointment appointment
    ) {

        if (appointment == null
                || appointment.getStatus() == null
                || appointment.getStatus().getCode() == null) {
            return "GRAY";
        }

        return switch (appointment.getStatus().getCode()) {
            case AppointmentStatusCode.CONFIRMED -> "GREEN";
            case AppointmentStatusCode.IN_PROGRESS -> "BLUE";
            case AppointmentStatusCode.CANCELLED -> "RED";
            case AppointmentStatusCode.LOCKED -> "YELLOW";
            default -> "GRAY";
        };
    }

}