package mx.com.bossdental.api.appointments.mapper;

import mx.com.bossdental.api.appointments.dto.response.AppointmentResponse;
import mx.com.bossdental.api.appointments.dto.response.LockAppointmentResponse;
import mx.com.bossdental.api.appointments.dto.response.UpdateAppointmentDentistResponse;
import mx.com.bossdental.api.appointments.dto.response.UpdateAppointmentStartTimeResponse;
import mx.com.bossdental.api.appointments.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

}