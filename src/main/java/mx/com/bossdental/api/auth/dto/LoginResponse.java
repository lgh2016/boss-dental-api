package mx.com.bossdental.api.auth.dto;

import mx.com.bossdental.api.users.dto.UserResponse;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}