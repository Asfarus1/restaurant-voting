package voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.Restaurant;

public interface RestaurantRepository extends AuthoriseUpdatePagingRepository<Restaurant> {

    @RestResource(path = "title", rel = "Search by title contains substring")
    Page<Restaurant> findByTitleContaining(String substring, Pageable pageable);
}
