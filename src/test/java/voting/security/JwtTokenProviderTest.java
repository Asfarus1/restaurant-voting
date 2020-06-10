package voting.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import voting.TestData;
import voting.security.exceptions.TokenAuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(60000, "secret");
    }

    @Test
    void generateToken() {
        String username = "username";
        String token = tokenProvider.generateToken(username);
        assertThat(tokenProvider.validateAndGetUsername(token)).isEqualTo(username);
    }

    @Test
    void isSignedWith() {
        assertThat(tokenProvider.isSignedWith(TestData.USER_ACCESS_TOKEN)).isTrue();
        assertThat(tokenProvider.isSignedWith("-----")).isFalse();
    }

    @Test
    void throwIfExpired() {
        tokenProvider = new JwtTokenProvider(1, "secret");
        String token = tokenProvider.generateToken("username");
        try {
            Thread.sleep(2L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThrows(TokenAuthenticationException.class, () -> tokenProvider.validateAndGetUsername(token));
    }
}