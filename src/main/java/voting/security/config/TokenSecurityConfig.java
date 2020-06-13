package voting.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Order(TokenSecurityConfig.ORDER)
public class TokenSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final int ORDER = 1;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/auth/**").httpBasic().realmName("auth")
                .and().csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS);
    }
}
