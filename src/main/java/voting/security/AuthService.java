package voting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import voting.domain.User;
import voting.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).map(this::toPrincipal).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private AuthUser toPrincipal(User user) {
        return new AuthUser(user.getId(), user.getUsername(),
                user.getPassword(), user.isEnabled(), user.getRoles());
    }
}
