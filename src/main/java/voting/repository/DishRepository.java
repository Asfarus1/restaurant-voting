package voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.Dish;
import voting.repository.internal.AuthoriseUpdatePagingRepository;

public interface DishRepository extends AuthoriseUpdatePagingRepository<Dish> {

    @RestResource(path = "title", rel = "Search by title contains substring")
    Page<Dish> findByTitleContaining(String substring, Pageable pageable);
}
