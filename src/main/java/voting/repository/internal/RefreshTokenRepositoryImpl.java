package voting.repository.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import voting.repository.RefreshTokenRepository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JdbcTemplate template;

    @Override
    public void deleteAllByUserId(Long userId) {
        template.update("DELETE FROM refresh_tokens WHERE user_id=?1", userId);
    }

    @Override
    public boolean update(Long userId, String oldToken, String newToken, Date expired) {
        return template.update(
                "UPDATE refresh_tokens SET token = ?1, expired=?2 WHERE user_id=?3 AND token=?4 AND expired>?5",
                newToken, expired, userId, oldToken, new Date()) == 1;
    }

    @Override
    public void add(Long userId, String token, Date expired) {
        template.update("INSERT INTO refresh_tokens VALUES (?1,?2,?3)", userId, token, expired);
    }
}
