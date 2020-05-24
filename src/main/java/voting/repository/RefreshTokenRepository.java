package voting.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;

//@RepositoryRestResource(exported = false)
public interface RefreshTokenRepository {

    void deleteAllByUserId(Long userId);

    boolean update(Long userId, String oldToken, String newToken, Date expired);

    void add(Long userId, String token, Date expired);
}
