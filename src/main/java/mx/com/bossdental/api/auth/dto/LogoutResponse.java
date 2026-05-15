package mx.com.bossdental.api.auth.dto;

public record LogoutResponse(
        boolean success,
        String message
) {
}