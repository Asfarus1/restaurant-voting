package voting.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import voting.domain.BaseEntity;

//import org.springframework.security.access.prepost.PreAuthorize;

@NoRepositoryBean
public interface AuthoriseUpdatePagingRepository<T extends BaseEntity> extends PagingAndSortingRepository<T, Long> {

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    <S extends T> S save(S entity);

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    void delete(T entity);

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    void deleteAll();

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(Long aLong);
}
