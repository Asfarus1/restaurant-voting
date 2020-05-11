package voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import voting.domain.Lunch;
import voting.domain.User;

import java.time.LocalDate;
import java.util.Optional;

public interface LunchRepository extends PagingAndSortingRepository<Lunch, Long> {
    Optional<Lunch> findByUserAndDate(User user, LocalDate date);

    @PreAuthorize("ADMIN")
    Iterable<Lunch> findAll(Sort sort);

    @PreAuthorize("ADMIN")
    Page<Lunch> findAll(Pageable pageable);

    @PreAuthorize("ADMIN")
    Iterable<Lunch> findAll();
}
