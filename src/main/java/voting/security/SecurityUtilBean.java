package voting.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import voting.domain.Role;

import java.util.Optional;

@Component
public class SecurityUtilBean {

    public Optional<AuthUser> getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(AuthUser.class::isInstance)
                .map(AuthUser.class::cast);
    }

    public boolean hasRole(Role role) {
        return getUser()
                .map(AuthUser::getAuthorities)
                .filter(roles -> roles.contains(role))
                .isPresent();
    }

    /**
     * @return authorized user id
     * @throws TokenAuthenticationException if user is not authorized
     */
    public Long getUserId() {
        return getUser().map(AuthUser::getId)
                .orElseThrow(() -> new TokenAuthenticationException("Not authorized"));
    }
}
