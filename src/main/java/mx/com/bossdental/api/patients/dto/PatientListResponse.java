package mx.com.bossdental.api.patients.dto;

public record PatientListResponse(
        Long id,
        String patientNumber,
        String fullName,
        String phone,
        String email,
        String photoUrl,
        Boolean active
) {
}