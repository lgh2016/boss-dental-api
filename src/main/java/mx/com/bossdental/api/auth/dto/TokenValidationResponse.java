package mx.com.bossdental.api.auth.dto;

public record TokenValidationResponse(
        boolean valid,
        String email
) {
}