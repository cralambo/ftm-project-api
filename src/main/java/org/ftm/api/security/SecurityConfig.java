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
                        .requestMatchers( "/auth/**", "/actuator/health").permitAll()
                        .requestMatchers( "/users/**" )
                        .authenticated()
                        .requestMatchers( "/roles/**" )
                        .authenticated()
                        .requestMatchers( "/permissions/**" )
                        .authenticated()
                        .requestMatchers( "/regions/**" )
                        .authenticated()
                        .requestMatchers( "/districts/**" )
                        .authenticated()
                        .requestMatchers( "/municipalitites/**" )
                        .authenticated()
                        .requestMatchers( "/landscapes/**" )
                        .authenticated()
                        .requestMatchers( "/sub-landscapes/**" )
                        .authenticated()
                        .requestMatchers( "/departments/**" )
                        .authenticated()
                        .requestMatchers( "/positions/**" )
                        .authenticated()
                        .requestMatchers( "/employees/**" )
                        .authenticated()
                        .requestMatchers( "/board-members/**" )
                        .authenticated()
                        .requestMatchers( "/organizations/**" )
                        .authenticated()
                        .requestMatchers( "/partners/**" )
                        .authenticated()
                        .requestMatchers( "/partner-contacts/**" )
                        .authenticated()
                        .requestMatchers( "/strategic-plans/**" )
                        .authenticated()
                        .requestMatchers( "/topics/**" )
                        .authenticated()
                        .requestMatchers( "/co-funders/**" )
                        .authenticated()
                        .requestMatchers("/partner-reports/**")
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
                List.of("http://localhost:3000", "http://localhost:5173", "http://localhost:4000", "http://192.168.88.64:4000")
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
