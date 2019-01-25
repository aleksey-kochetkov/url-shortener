package url_shortener.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
                                                       throws Exception {
        auth.inMemoryAuthentication()
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .withUser("user")
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
