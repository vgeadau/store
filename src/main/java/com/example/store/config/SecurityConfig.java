package com.example.store.config;

import com.example.store.security.JwtRequestFilter;
import com.example.store.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.store.util.Constants.*;

/**
 * Configuration class where we set that the operations CREATE, UPDATE and DELETE are available
 * only to authenticated users.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * At this configuration method we set which endpoints are permitted to be accessed without
     * authentication and which are not.
     * @param http HttpSecurity
     * @throws Exception in case of error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTHENTICATE).permitAll()
                .antMatchers(HttpMethod.GET, STORE_EXT).permitAll()
                .antMatchers(HttpMethod.POST, STORE).authenticated()
                .antMatchers(HttpMethod.PUT, STORE_EXT).authenticated()
                .antMatchers(HttpMethod.DELETE, STORE_EXT).authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}