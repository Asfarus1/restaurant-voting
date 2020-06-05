package voting.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Order(1)
public class TokenSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/auth/**").httpBasic().realmName("auth")
                .and().csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and().authorizeRequests()
                .antMatchers("/auth/refresh_token").permitAll()
                .antMatchers("/auth/create_token", "/auth/logout").authenticated();
    }
}
