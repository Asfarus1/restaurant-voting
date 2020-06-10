package voting.security.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import voting.domain.Role;
import voting.security.SecurityUtilBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserFilter extends OncePerRequestFilter {
    private static final Pattern USER_ID_PATTERN = Pattern.compile("(?<=/users/)\\d+");
    private final SecurityUtilBean securityUtilBean;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("UserFilter:" + securityUtilBean.getUser().orElse(null));
        if (!securityUtilBean.hasRole(Role.ROLE_ADMIN)) {
            String url = request.getRequestURL().toString();
            Optional<Long> requestedUserId = Optional.of(USER_ID_PATTERN.matcher(url))
                    .filter(Matcher::find)
                    .map(Matcher::group)
                    .map(Long::parseLong);
            if (requestedUserId.isPresent()) {
                Long userId = requestedUserId.get();
                if (!userId.equals(securityUtilBean.getUserId())) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
