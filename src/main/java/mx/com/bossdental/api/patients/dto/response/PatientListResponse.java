package mx.com.bossdental.api.patients.dto.response;

public record PatientListResponse(
        Long id,
        String expedientNumber,
        String fullName,
        String phone,
        String email,
        String photoUrl,
        Boolean active
) {
}