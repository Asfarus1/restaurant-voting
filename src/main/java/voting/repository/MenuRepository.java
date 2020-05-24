package voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.rest.core.annotation.RestResource;
import voting.domain.Menu;
import voting.repository.internal.AuthoriseUpdatePagingRepository;
import voting.repository.internal.MenuCustomSaveRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MenuRepository extends AuthoriseUpdatePagingRepository<Menu>, MenuCustomSaveRepository {

    @RestResource(path = "date", rel = "Search by date equals")
    @EntityGraph(Menu.WITH_ITEMS)
    <S extends Menu> Page<S> findAllByDate(LocalDate date, Pageable pageable);

    @Override
    @EntityGraph(Menu.WITH_ITEMS)
    Page<Menu> findAll(Pageable pageable);

    @Override
    @EntityGraph(Menu.WITH_ITEMS)
    Optional<Menu> findById(Long id);
}
