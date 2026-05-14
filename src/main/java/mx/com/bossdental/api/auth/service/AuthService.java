package mx.com.bossdental.api.auth.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.auth.dto.LoginRequest;
import mx.com.bossdental.api.auth.dto.LoginResponse;
import mx.com.bossdental.api.branches.dto.BranchResponse;
import mx.com.bossdental.api.roles.dto.RoleResponse;
import mx.com.bossdental.api.security.JwtService;
import mx.com.bossdental.api.users.dto.UserResponse;
import mx.com.bossdental.api.users.entity.User;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

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

        RoleResponse roleResponse = new RoleResponse(
                user.getRole().getId(),
                user.getRole().getName(),
                user.getRole().getDescription()
        );

        BranchResponse branchResponse = new BranchResponse(
                user.getBranch().getId(),
                user.getBranch().getName()
        );

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                roleResponse,
                branchResponse
        );

        return new LoginResponse(token, userResponse);
    }
}