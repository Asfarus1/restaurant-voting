package voting.repository;

import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.Lunch;

import java.time.LocalDate;
import java.util.Optional;

public interface LunchRepository extends AuthoriseUpdatePagingRepository<Lunch>, HaveLunchInRepository {
}
