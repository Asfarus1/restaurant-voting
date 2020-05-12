package voting.web;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import voting.security.AuthorizedUser;

import java.util.Optional;

public class SecurityUtil {

    public static Optional<AuthorizedUser> getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal).map(AuthorizedUser.class::cast);
    }

    @Nullable
    public static Long getUserId() {
        return getUser().map(AuthorizedUser::getId).orElse(null);
    }
}
