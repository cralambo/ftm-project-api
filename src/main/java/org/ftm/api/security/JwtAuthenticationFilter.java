package org.ftm.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        /*
         * Aucun token :
         * on laisse Spring Security poursuivre normalement.
         */
        if (!StringUtils.hasText(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Header Authorization présent mais mauvais format.
         */
        if (!authorizationHeader.startsWith("Bearer ")) {

            log.debug(
                    "Authorization header has invalid format - URI: {}",
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7).trim();

        if (!StringUtils.hasText(token)) {

            log.debug(
                    "Bearer token is empty - URI: {}",
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Si une authentification existe déjà,
         * inutile de reconstruire le SecurityContext.
         */
        if (SecurityContextHolder
                .getContext()
                .getAuthentication() != null) {

            filterChain.doFilter(request, response);
            return;
        }

        String username;

        /*
         * Extraction du username depuis le JWT.
         */
        try {

            username = jwtService.extractUsername(token);

        } catch (Exception exception) {

            log.warn(
                    "Unable to extract username from JWT - URI: {} - reason: {}",
                    request.getRequestURI(),
                    exception.getMessage()
            );

            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(username)) {

            log.warn(
                    "JWT does not contain a valid username - URI: {}",
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        log.debug(
                "JWT username extracted: {} - URI: {}",
                username,
                request.getRequestURI()
        );

        /*
         * Chargement de l'utilisateur depuis la base.
         */
        final UserDetails userDetails;

        try {

            userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

        } catch (Exception exception) {

            log.warn(
                    "User not found or unable to load user: {}",
                    username
            );

            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Validation complète du JWT :
         * - signature
         * - expiration
         * - username
         */
        try {

            if (!jwtService.isTokenValid(token, userDetails)) {

                log.warn(
                        "Invalid JWT for user: {}",
                        username
                );

                filterChain.doFilter(request, response);
                return;
            }

        } catch (Exception exception) {

            log.warn(
                    "JWT validation failed for user: {} - reason: {}",
                    username,
                    exception.getMessage()
            );

            filterChain.doFilter(request, response);
            return;
        }

        /*
         * JWT valide :
         * création de l'Authentication Spring Security.
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
