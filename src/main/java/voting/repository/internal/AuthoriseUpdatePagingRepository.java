package voting.repository.internal;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import voting.domain.BaseEntity;


@NoRepositoryBean
public interface AuthoriseUpdatePagingRepository<T extends BaseEntity> extends PagingAndSortingRepository<T, Long> {

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    <S extends T> S save(S entity);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(T entity);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void deleteAll();

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(Long aLong);
}
