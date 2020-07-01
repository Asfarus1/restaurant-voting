package voting.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import voting.security.JwtTokenFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Order(RestApiSecurityConfig.ORDER)
@RequiredArgsConstructor
public class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final int ORDER = TokenSecurityConfig.ORDER + 1;

    private final JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/rest-api/**")
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and().csrf().disable()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/rest-api/users", "/rest-api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
