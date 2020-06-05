package voting.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import voting.security.AuthService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth, AuthService authService) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/h2-console/**").
                authorizeRequests().
                antMatchers("/h2-console/**").permitAll();
    }
}
