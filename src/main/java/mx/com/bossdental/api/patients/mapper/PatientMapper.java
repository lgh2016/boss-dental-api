package mx.com.bossdental.api.patients.mapper;

import mx.com.bossdental.api.patients.dto.PatientCreateRequest;
import mx.com.bossdental.api.patients.dto.PatientListResponse;
import mx.com.bossdental.api.patients.dto.PatientResponse;
import mx.com.bossdental.api.patients.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    PatientListResponse toListResponse(Patient patient);
}