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
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
                                                       throws Exception {
        auth.jdbcAuthentication().dataSource(this.dataSource)
        .usersByUsernameQuery("SELECT account_id, password, TRUE "
                            + "FROM account WHERE account_id = ?")
        .authoritiesByUsernameQuery("SELECT account_id, account_id "
                                  + "FROM account WHERE account_id = ?")
        .passwordEncoder(NoOpPasswordEncoder.getInstance());
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

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
