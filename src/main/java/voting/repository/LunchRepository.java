package voting.repository;

import voting.domain.Lunch;

public interface LunchRepository extends AuthoriseUpdatePagingRepository<Lunch>, HaveLunchInRepository {
}
