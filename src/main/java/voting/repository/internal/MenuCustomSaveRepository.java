package voting.repository.internal;

import voting.domain.Menu;

public interface MenuCustomSaveRepository {
    <S extends Menu> S save(S menu);
}
