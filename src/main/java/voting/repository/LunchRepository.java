package voting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.Lunch;
import voting.repository.internal.HaveLunchInRepository;

public interface LunchRepository extends PagingAndSortingRepository<Lunch, Long>, HaveLunchInRepository {

    @Override
    @RestResource(exported = false)
    <S extends Lunch> S save(S entity);

    @Override
    @RestResource(exported = false)
    void delete(Lunch entity);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @RestResource(exported = false)
    void deleteById(Long aLong);
}
