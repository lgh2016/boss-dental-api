package mx.com.bossdental.api.auth.dto;

public record ChangePasswordResponse(
        boolean success,
        String message
) {
}