package voting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.User;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;

//@PreAuthorize("hasRole('ADMIN')")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @RestResource(path = "username", rel = "Search by username contains substring")
    Optional<User> findByUsername(String substring);
}
