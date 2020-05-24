package voting.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authHeader = ((HttpServletRequest) request).getHeader(AUTHORIZATION);
        if (authHeader != null && tokenProvider.isSignedWith(authHeader)) {
            try {
                String username = tokenProvider.validateAndGetUsername(authHeader);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                SecurityContextHolder.setContext(new SecurityContextImpl(toAuthToken(userDetails)));
            } catch (AuthenticationException ex) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, ex.getLocalizedMessage());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken toAuthToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
