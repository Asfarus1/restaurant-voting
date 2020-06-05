package voting.repository.internal;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import voting.domain.BaseEntity;


@NoRepositoryBean
public interface AuthoriseUpdatePagingRepository<T extends BaseEntity> extends PagingAndSortingRepository<T, Long> {

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    <S extends T> S save(S entity);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void delete(T entity);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void deleteAll();

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void deleteById(Long aLong);
}
