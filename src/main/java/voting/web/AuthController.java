package voting.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.domain.User;
import voting.repository.RefreshTokenRepository;
import voting.repository.UserRepository;
import voting.security.AuthUser;
import voting.security.JwtTokenProvider;
import voting.security.SecurityUtil;
import voting.security.TokenAuthenticationException;
import voting.web.dto.RefreshTokenRequest;
import voting.web.dto.TokenResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authManager;
    private final BasicAuthenticationConverter basicAuthConverter;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh-token.duration-millis}")
    private long refreshTokenDurationMs;

    @Value("${jwt.token.duration-millis}")
    private long accessTokenDurationMs;

    //unauthorized
    @PostMapping("/create_token")
    public TokenResponse getAccessToken(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = basicAuthConverter.convert(request);
        AuthUser user = (AuthUser) authManager.authenticate(auth).getPrincipal();
        TokenResponse tokenResponse = getTokenResponse(user.getUsername());
        refreshTokenRepository.add(user.getId(), tokenResponse.getRefreshToken(), tokenResponse.getRefreshExpired());
        return tokenResponse;
    }

    //unauthorized
    @PostMapping("/refresh_token")
    public TokenResponse refreshAccessToken(RefreshTokenRequest tokenRequest) {
        String username = tokenRequest.getUsername();
        Long userId = repository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User with name" + username + " not found"));
        TokenResponse tokenResponse = getTokenResponse(username);
        if (!refreshTokenRepository.update(userId,
                tokenRequest.getRefreshToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getRefreshExpired())) {
            throw new TokenAuthenticationException("Refresh token not found or expired");
        }
        return tokenResponse;
    }

    //authorized
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Long userId = SecurityUtil.getUserId();
        refreshTokenRepository.deleteAllByUserId(userId);
        return ResponseEntity.accepted().build();
    }

    private TokenResponse getTokenResponse(String username) {
        Date now = new Date();
        TokenResponse response = new TokenResponse();
        response.setAccessToken(tokenProvider.generateToken(username));
        response.setRefreshToken(UUID.randomUUID().toString());
        response.setAccessExpired(new Date(now.getTime() + accessTokenDurationMs));
        response.setRefreshExpired(new Date(now.getTime() + refreshTokenDurationMs));
        return response;
    }
}