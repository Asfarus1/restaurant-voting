package voting.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import voting.domain.User;
import voting.repository.UserRepository;

@Component
public class AuthService implements UserDetailsService {

    private final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).map(this::toPrincipal).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private AuthorizedUser toPrincipal(User user) {
        return new AuthorizedUser(user.getId(), user.getUsername(),
                user.getPassword(), user.isEnabled(), user.getRoles());
    }
}
