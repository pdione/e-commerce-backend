package com.example.securitydemo.config;

import com.example.securitydemo.jwt.AuthEntryPointJwt;
import com.example.securitydemo.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests((requests) ->
                        // requests.requestMatchers("/h2-console/**").permitAll() Spring Security 6
                        requests.requestMatchers(toH2Console()).permitAll()
                                .requestMatchers("/signin").permitAll()
                                .anyRequest().authenticated()
                )
//
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers(toH2Console())
//                )
                .csrf(AbstractHttpConfigurer::disable)
                // To allow frames from the same origin
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        // To make the request stateless and disabling cookies
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //http.formLogin(withDefaults());
        //http.httpBasic(withDefaults());
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public CommandLineRunner initData(UserDetailsService userDetailsService) {
        return (args) -> {

            JdbcUserDetailsManager jdbcUserDetailsManager = (JdbcUserDetailsManager) userDetailsService;
            UserDetails user = User.withUsername("user1")
                    // To tell Spring that this is a plain text password
                    .password(passwordEncoder().encode("password1"))
                    .roles("USER")
                    .build();

            UserDetails admin = User.withUsername("admin")
                    // To tell Spring that this is a plain text password
                    .password(passwordEncoder().encode("adminPass"))
                    .roles("ADMIN")
                    .build();
            jdbcUserDetailsManager.createUser(user);
            jdbcUserDetailsManager.createUser(admin);
        };
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) {
        return builder.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
