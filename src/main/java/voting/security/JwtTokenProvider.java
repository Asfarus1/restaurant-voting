package voting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer_";
    private final long jwtTokenDurationMs;
    private final String secret;

    public JwtTokenProvider(@Value("${jwt.token.duration-millis}") long jwtTokenDurationMs,
                            @Value("${jwt.token.secret}") String secret) {
        this.jwtTokenDurationMs = jwtTokenDurationMs;
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateToken(String username) {
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtTokenDurationMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return TOKEN_PREFIX + token;
    }

    public boolean isSignedWith(String token) {
        return token.startsWith(TOKEN_PREFIX);
    }

    /**
     * @param token access token string
     * @return username username from token
     * @throws TokenAuthenticationException if not valid or expired
     */
    public String validateAndGetUsername(String token) {
        return getClaims(token.substring(TOKEN_PREFIX.length())).getSubject();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token " + token + " token is expired or invalid", e);
            throw new TokenAuthenticationException("JWT token is expired or invalid");
        }
    }
}
