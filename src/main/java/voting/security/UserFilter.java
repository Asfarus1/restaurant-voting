package voting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import voting.domain.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserFilter extends GenericFilterBean {
    private static final Pattern USER_ID_PATTERN = Pattern.compile("(?<=/users/)\\d+");
    private final SecurityUtilBean securityUtilBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!securityUtilBean.hasRole(Role.ADMIN)) {
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            Optional<Long> requestedUserId = Optional.of(USER_ID_PATTERN.matcher(url))
                    .filter(Matcher::find)
                    .map(Matcher::group)
                    .map(Long::parseLong);
            if (requestedUserId.isPresent()) {
                Long userId = requestedUserId.get();
                if (!userId.equals(securityUtilBean.getUserId())) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}
