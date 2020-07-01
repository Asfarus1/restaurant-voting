package voting.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(AdminSecurityConfig.ORDER)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final int ORDER = RestApiSecurityConfig.ORDER + 1;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().realmName("admin")
                .and().formLogin()
                .and().logout()
                .and().csrf().disable()
                .headers().frameOptions().disable()
                .and().authorizeRequests()
                .antMatchers("/h2-console/**").hasRole("ADMIN");
    }
}
