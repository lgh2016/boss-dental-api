package mx.com.bossdental.api.patients.mapper;

import mx.com.bossdental.api.patients.dto.request.PatientCreateRequest;
import mx.com.bossdental.api.patients.dto.response.PatientDetailResponse;
import mx.com.bossdental.api.patients.dto.response.PatientListResponse;
import mx.com.bossdental.api.patients.dto.response.PatientResponse;
import mx.com.bossdental.api.patients.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toEntity(PatientCreateRequest request);

    PatientResponse toResponse(Patient patient);

    @Mapping(
            target = "fullName",
            expression = "java(patient.getName() + \" \" + patient.getLastName())"
    )
    @Mapping(
            target = "photoUrl",
            expression = """
                    java(
                        patient.getPhotoUrl() != null
                        && !patient.getPhotoUrl().isBlank()
                        ? patient.getPhotoUrl()
                        : mx.com.bossdental.api.common.constants.AppConstants.DEFAULT_PATIENT_PHOTO_URL
                    )
                    """
    )
    @Mapping(
            target = "expedientNumber",
            source = "clinicalRecord.expedientNumber"
    )
    PatientListResponse toListResponse(Patient patient);

    /**
     * Convierte la entidad Patient a la respuesta base
     * del detalle del panel lateral.
     *
     * Los datos calculados o externos como citas, saldos
     * y doctor se completan desde el servicio.
     *
     * @param patient entidad del paciente
     * @return respuesta base del detalle del paciente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "expedientNumber", source = "clinicalRecord.expedientNumber")
    @Mapping(target = "fullName", expression = "java(buildFullName(patient.getName(), patient.getLastName()))")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "age", expression = "java(calculateAge(patient.getBirthDate()))")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "location", source = "address")
    @Mapping(target = "avatarUrl", source = "photoUrl")
    @Mapping(target = "initials", expression = "java(buildInitials(patient.getName(), patient.getLastName()))")
    @Mapping(target = "doctorName", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "paidAmount", ignore = true)
    @Mapping(target = "totalBudgeted", ignore = true)
    @Mapping(target = "previousAppointment", ignore = true)
    @Mapping(target = "nextAppointment", ignore = true)
    PatientDetailResponse toDetailResponse(Patient patient);

    /**
     * Construye el nombre completo del paciente.
     *
     * @param name nombre
     * @param lastName apellido
     * @return nombre completo sin valores nulos
     */
    default String buildFullName(String name, String lastName) {
        return Stream.of(name, lastName)
                .filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .collect(Collectors.joining(" "));
    }

    /**
     * Genera las iniciales del paciente para mostrar
     * un avatar genérico cuando no exista imagen de perfil.
     *
     * @param name nombre
     * @param lastName apellido
     * @return iniciales en mayúsculas
     */
    default String buildInitials(String name, String lastName) {
        String firstInitial = name != null && !name.isBlank()
                ? name.substring(0, 1)
                : "";

        String secondInitial = lastName != null && !lastName.isBlank()
                ? lastName.substring(0, 1)
                : "";

        return (firstInitial + secondInitial).toUpperCase();
    }

    /**
     * Calcula la edad del paciente a partir de su fecha
     * de nacimiento.
     *
     * @param birthDate fecha de nacimiento
     * @return edad calculada o null si no existe fecha
     */
    default Integer calculateAge(LocalDate birthDate) {
        return birthDate != null
                ? Period.between(birthDate, LocalDate.now()).getYears()
                : null;
    }
}