package mx.com.bossdental.api.auth.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.auth.dto.*;
import mx.com.bossdental.api.auth.service.AuthService;
import mx.com.bossdental.api.users.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(
                authService.getAuthenticatedUser(authentication.getName())
        );
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                authService.validateToken(authentication.getName())
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        return ResponseEntity.ok(
                authService.changePassword(
                        authentication.getName(),
                        request
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {

        return ResponseEntity.ok(
                authService.logout()
        );
    }


}