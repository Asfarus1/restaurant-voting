package voting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import voting.domain.Menu;

public interface MenuRepository extends PagingAndSortingRepository<Menu, Long> {
}
