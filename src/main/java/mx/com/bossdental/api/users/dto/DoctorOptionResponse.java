package mx.com.bossdental.api.users.dto;

public record DoctorOptionResponse(
        Long id,
        String fullName,
        String specialty
) {
}