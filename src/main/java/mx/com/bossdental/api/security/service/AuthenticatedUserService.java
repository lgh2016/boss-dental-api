package mx.com.bossdental.api.security.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.users.entity.User;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de obtener
 * información del usuario autenticado.
 */
@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserRepository userRepository;

    /**
     * Obtiene el usuario autenticado.
     *
     * @return usuario autenticado.
     */
    public User getAuthenticatedUser() {

        /*
         * Obtener autenticación actual.
         */
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getName() == null) {
            return null;
        }

        /*
         * Buscar usuario por email/username.
         */
        return userRepository.findByEmail(
                        authentication.getName()
                )
                .orElse(null);
    }

    /**
     * Obtiene el ID del usuario autenticado.
     *
     * @return ID del usuario autenticado.
     */
    public Long getAuthenticatedUserId() {

        User user = getAuthenticatedUser();

        if (user == null) {
            return null;
        }

        return user.getId();
    }
}