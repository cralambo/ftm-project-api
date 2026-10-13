package org.ftm.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/auth/**", "/api/v1/auth/**", "/actuator/health").permitAll()
                        .requestMatchers( "/api/v1/users/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/roles/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/permissions/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/regions/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/districts/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/municipalitites/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/landscapes/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/sub-landscapes/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/departments/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/positions/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/employees/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/board-members/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/organizations/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/partners/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/partner-contacts/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/strategic-plans/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/topics/**" )
                        .authenticated()
                        .requestMatchers( "/api/v1/co-funders/**" )
                        .authenticated()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:3000", "http://localhost:5173", "http://localhost:4000")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}
