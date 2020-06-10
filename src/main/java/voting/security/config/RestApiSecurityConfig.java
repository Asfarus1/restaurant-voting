package voting.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import voting.security.filters.JwtTokenFilter;
import voting.security.filters.UserFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Order(2)
@RequiredArgsConstructor
public class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;
    private final UserFilter userFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/rest-api/**")
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and().csrf().disable()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userFilter, FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers("/rest-api/users", "/rest-api/users/").hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
