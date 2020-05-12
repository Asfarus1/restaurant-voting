package voting.repository;

import voting.domain.Menu;

public interface UpdateMenuRepository {
    void updateMenu(Long restaurant, Menu menu);
}
