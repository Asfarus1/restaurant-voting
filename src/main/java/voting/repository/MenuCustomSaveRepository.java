package voting.repository;

import voting.domain.Menu;

public interface MenuCustomSaveRepository {
    <S extends Menu> S save(S menu);
}
