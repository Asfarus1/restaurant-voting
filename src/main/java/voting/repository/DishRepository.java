package voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.Dish;
import voting.repository.internal.AuthoriseUpdatePagingRepository;

import javax.persistence.QueryHint;

import static org.hibernate.annotations.QueryHints.CACHEABLE;
import static org.hibernate.annotations.QueryHints.CACHE_REGION;

public interface DishRepository extends AuthoriseUpdatePagingRepository<Dish> {

    @RestResource(path = "title", rel = "Search by title contains substring")
    Page<Dish> findByTitleContaining(String substring, Pageable pageable);

    @Override
    @QueryHints({@QueryHint(name = CACHEABLE, value = "true"),
            @QueryHint(name = CACHE_REGION, value = "dishes")})
    Page<Dish> findAll(Pageable pageable);
}
