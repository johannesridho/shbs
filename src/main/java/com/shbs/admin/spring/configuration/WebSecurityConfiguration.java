package com.shbs.admin.spring.configuration;

import com.shbs.admin.user.authority.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/admin/**")
            .authorizeRequests()
                .antMatchers("/admin", "/admin/").authenticated()
                .antMatchers("/admin/user/change-password").authenticated()

                .antMatchers("/admin/oauth-client/**").hasRole(Role.ADMIN.name())

                // This is the 'guard' code that make sure every URL must be protected
                .antMatchers("/admin/**").hasAuthority("GUARD")
            .and()

            .formLogin()
                .loginPage("/admin/login")
                .usernameParameter("username").passwordParameter("password")
                .failureUrl("/admin/login?error")
                .defaultSuccessUrl("/admin")
                .permitAll()
            .and()

            .logout()
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout")
            .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(passwordEncoder()).dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
