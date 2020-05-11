package voting.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import voting.domain.MenuItem;

public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem,Long> {
}
