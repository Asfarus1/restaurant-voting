package voting.repository;

import voting.domain.Lunch;
import voting.repository.internal.AuthoriseUpdatePagingRepository;
import voting.repository.internal.HaveLunchInRepository;

public interface LunchRepository extends AuthoriseUpdatePagingRepository<Lunch>, HaveLunchInRepository {
}
