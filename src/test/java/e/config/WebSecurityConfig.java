package e.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
                                                       throws Exception {
        auth.inMemoryAuthentication()
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .withUser("user")
// In spring-security-core:5.0.0.RC1, the default PasswordEncoder is built as a DelegatingPasswordEncoder.
// Который требует задать id для обращения к конкретному PasswordEncoder.
// Без id падает. "{noop}" это способ задать id. NoOpPasswordEncoder в
// данном случае. Это аналогично закомментированному выше методу
// passwordEncoder(PasswordEncoder). Однако NoOpPasswordEncoder помечен
// как depricated.
            .password("password")
            .authorities("user")
            .and()
            .withUser("test").password("{noop}test").roles("ADMIN");
     }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/register").authenticated()
                                .antMatchers("/statistic/{accountId}")
                                      .access("hasAuthority(#accountId)")
                                .anyRequest().permitAll()
                                .and().httpBasic()
                                .and().csrf().disable();
    }
}
