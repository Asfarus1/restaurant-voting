package voting.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import voting.domain.Role;

import java.util.Optional;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static Optional<AuthUser> getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal).map(AuthUser.class::cast);
    }

    public static boolean hasRole(Role role){
        return getUser()
                .map(AuthUser::getAuthorities)
                .filter(roles -> roles.contains(role))
                .isPresent();
    }

    /**
     * @return authorized user id
     * @throws TokenAuthenticationException if user is not authorized
     */
    public static Long getUserId() {
        return getUser().map(AuthUser::getId)
                .orElseThrow(() -> new TokenAuthenticationException("Not authorized"));
    }
}
