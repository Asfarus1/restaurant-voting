package voting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.User;

import javax.persistence.QueryHint;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.CACHEABLE;
import static org.hibernate.annotations.QueryHints.CACHE_REGION;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @RestResource(path = "username", rel = "Search by username contains substring")
    @QueryHints({@QueryHint(name = CACHEABLE, value = "true"),
            @QueryHint(name = CACHE_REGION, value = "users")})
    Optional<User> findByUsername(String substring);
}
