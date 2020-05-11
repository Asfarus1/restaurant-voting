package voting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import voting.domain.Dish;
import voting.domain.Restaurant;

public interface DishRepository extends PagingAndSortingRepository<Dish, Long> {
}
