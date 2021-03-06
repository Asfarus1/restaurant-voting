package voting.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {
    @Getter
    private final Long id;

    public AuthUser(Long id, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, enabled, enabled, enabled, authorities);
        this.id = id;
    }
}
