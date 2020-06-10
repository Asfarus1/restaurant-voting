package voting.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.domain.Role;
import voting.domain.User;
import voting.repository.RefreshTokenRepository;
import voting.repository.UserRepository;
import voting.security.AuthUser;
import voting.security.JwtTokenProvider;
import voting.security.SecurityUtilBean;
import voting.security.exceptions.TokenAuthenticationException;
import voting.web.dto.RefreshTokenRequest;
import voting.web.dto.SignUpRequest;
import voting.web.dto.TokenResponse;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityUtilBean securityUtilBean;
    private final PasswordEncoder passwordEncoder;

    @Value("${refresh-token.duration-millis}")
    private long refreshTokenDurationMs;

    @Value("${jwt.token.duration-millis}")
    private long accessTokenDurationMs;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create_token")
    public TokenResponse getAccessToken() {
        AuthUser user = securityUtilBean.getUser().orElseThrow(() -> new BadCredentialsException("Unauthorized"));
        TokenResponse tokenResponse = getTokenResponse(user.getUsername());
        refreshTokenRepository.add(user.getId(), tokenResponse.getRefreshToken(), tokenResponse.getRefreshExpired());
        return tokenResponse;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/refresh_token")
    public TokenResponse refreshAccessToken(@Valid @RequestBody RefreshTokenRequest tokenRequest) {
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

    @PreAuthorize("permitAll()")
    @PostMapping("/sign_up")
    public TokenResponse signUp(@Valid @RequestBody SignUpRequest userRequest) {
        String password = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getName(), password, Role.ROLE_USER);
        repository.save(user);
        TokenResponse tokenResponse = getTokenResponse(user.getUsername());
        refreshTokenRepository.add(user.getId(), tokenResponse.getRefreshToken(), tokenResponse.getRefreshExpired());
        return tokenResponse;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Long userId = securityUtilBean.getUserId();
        refreshTokenRepository.deleteAllByUserId(userId);
        SecurityContextHolder.clearContext();
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
