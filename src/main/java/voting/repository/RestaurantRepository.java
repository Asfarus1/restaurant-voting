package voting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import voting.domain.Restaurant;

public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, Long> {
}
