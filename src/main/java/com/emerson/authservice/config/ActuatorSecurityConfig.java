package com.emerson.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
public class ActuatorSecurityConfig {

    @Value("${management.security.roles:ACTUATOR_ADMIN}")
    private String actuatorRole;

    @Value("${actuator.username:actuator}")
    private String actuatorUsername;

    @Value("${actuator.password:${ACTUATOR_PASSWORD:actuator}}")
    private String actuatorPassword;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(new AntPathRequestMatcher("/actuator/**"))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/health/**").hasRole(actuatorRole)
                .requestMatchers("/actuator/**").hasRole(actuatorRole)
            )
            .authenticationProvider(actuatorAuthenticationProvider())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .xssProtection(xss -> {})
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
            );

        if ("prod".equals(activeProfile)) {
            http.requiresChannel(channel -> channel
                .anyRequest().requiresSecure()
            );
        }

        return http.build();
    }

    @Bean(name = "actuatorAuthenticationProvider")
    public DaoAuthenticationProvider actuatorAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(actuatorUserDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean(name = "actuatorUserDetailsService")
    public InMemoryUserDetailsManager actuatorUserDetailsService() {
        UserDetails actuatorUser = User.builder()
            .username(actuatorUsername)
            .password(passwordEncoder().encode(actuatorPassword))
            .roles(actuatorRole)
            .build();

        return new InMemoryUserDetailsManager(actuatorUser);
    }

    @Bean(name = "actuatorPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 