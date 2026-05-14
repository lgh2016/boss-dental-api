package mx.com.bossdental.api.auth.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.auth.dto.LoginRequest;
import mx.com.bossdental.api.auth.dto.LoginResponse;
import mx.com.bossdental.api.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }
}