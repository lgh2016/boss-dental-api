package mx.com.bossdental.api.auth.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.auth.dto.*;
import mx.com.bossdental.api.branches.dto.BranchResponse;
import mx.com.bossdental.api.roles.dto.RoleResponse;
import mx.com.bossdental.api.security.JwtService;
import mx.com.bossdental.api.users.dto.UserResponse;
import mx.com.bossdental.api.users.entity.User;
import mx.com.bossdental.api.users.mapper.UserMapper;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserMapper userMapper;


    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        boolean validPassword = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!validPassword) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserResponse userResponse = userMapper.toResponse(user);

        return new LoginResponse(token, refreshToken, userResponse);
    }

    /**
     * Obtiene el usuario autenticado por su email
     * @param email
     * @return
     */
    public UserResponse getAuthenticatedUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return userMapper.toResponse(user);
    }

    public TokenValidationResponse validateToken(String email) {

        return new TokenValidationResponse(
                true,
                email
        );
    }

    public ChangePasswordResponse changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        boolean validCurrentPassword = passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        );

        if (!validCurrentPassword) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );

        userRepository.save(user);

        return new ChangePasswordResponse(
                true,
                "Contraseña actualizada correctamente"
        );
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh accessToken inválido");
        }

        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmailWithRole(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        String newToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new RefreshTokenResponse(
                newToken,
                newRefreshToken
        );
    }

    public LogoutResponse logout() {

        return new LogoutResponse(
                true,
                "Sesión cerrada correctamente"
        );
    }
}