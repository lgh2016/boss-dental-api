package mx.com.bossdental.api.appointments.mapper;

import mx.com.bossdental.api.appointments.dto.response.PatientAppointmentResponse;
import mx.com.bossdental.api.appointments.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para transformar citas de paciente
 * a DTO de respuesta.
 */
@Mapper(componentModel = "spring")
public interface PatientAppointmentMapper {

    /**
     * Convierte una cita en respuesta para frontend.
     *
     * @param appointment entidad de cita.
     * @return DTO de cita del paciente.
     */
    @Mapping(target = "doctorName", expression = "java(buildDoctorName(appointment))")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "statusCode", source = "status.code")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "statusColor", ignore = true)
    @Mapping(target = "canCancel", ignore = true)
    @Mapping(target = "canReschedule", ignore = true)
    PatientAppointmentResponse toResponse(Appointment appointment);

    /**
     * Construye el nombre visible del doctor.
     *
     * @param appointment entidad de cita.
     * @return nombre completo del doctor.
     */
    default String buildDoctorName(Appointment appointment) {

        if (appointment == null || appointment.getDentist() == null) {
            return null;
        }

        return appointment.getDentist().getName()
                + " "
                + appointment.getDentist().getLastName();
    }
}