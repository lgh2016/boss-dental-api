package mx.com.bossdental.api.auth.dto;

public record RefreshTokenResponse(
        String token,
        String refreshToken
) {
}