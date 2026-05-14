package mx.com.bossdental.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mx.com.bossdental.api.users.dto.UserResponse;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private UserResponse user;
}