package org.ftm.api.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ftm.api.refresh_token.RefreshToken;
import org.ftm.api.refresh_token.RefreshTokenRepository;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.security.JwtService;
import org.ftm.api.user.User;
import org.ftm.api.user.UserAccountService;
import org.ftm.api.user.UserRepository;
import org.ftm.api.user.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final String REFRESH_COOKIE = "refresh_token";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountService userAccountService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.security.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${app.security.cookie-secure}")
    private boolean cookieSecure;

    public LoginResponse login(
            LoginRequest request,
            HttpServletResponse response
    ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            throw new IllegalStateException( "Utilisateur authentifié introuvable" );
        }

        String token = jwtService.generateToken(userDetails);

        String refreshToken = generateRefreshToken();

        saveRefreshToken(
                userDetails.getId(),
                refreshToken
        );

        addRefreshCookie(response, refreshToken);

        return buildLoginResponse(userDetails, token);
    }

    @Transactional
    public String register(RegisterRequest request) {
        User user;
        if (UserType.valueOf(request.user_type()) == UserType.ADMIN) {
            user = userAccountService.createAdminAccount(
                    request.email(),
                    request.firstname(),
                    request.lastname()
            );
        } else {
            user = userAccountService.createUserAccount(
                    request.email(),
                    request.firstname(),
                    request.lastname()
            );
        }
        return user.getId().toString();
    }

    @Transactional
    public LoginResponse refresh(HttpServletRequest request, HttpServletResponse response) {

        String rawRefreshToken = extractRefreshToken(request);

        if (rawRefreshToken == null) {
            throw new RuntimeException( "Refresh token manquant" );
        }
        String tokenHash = hashToken(rawRefreshToken);

        RefreshToken storedToken = refreshTokenRepository.findByTokenHashWithUser(tokenHash)
                .orElseThrow(() -> new RuntimeException( "Refresh token invalide" ) );

        if (storedToken.isRevoked()) {
            throw new RuntimeException( "Refresh token révoqué" );
        }

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            storedToken.setRevoked(true);
            refreshTokenRepository.save(storedToken);
            throw new RuntimeException( "Refresh token expiré" );
        }
        User user = storedToken.getUser();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String newAccessToken = jwtService.generateToken(userDetails);

        storedToken.setRevoked(true);

        refreshTokenRepository.save(storedToken);

        String newRefreshToken = generateRefreshToken();

        saveRefreshToken( user.getId(), newRefreshToken );

        addRefreshCookie( response, newRefreshToken );

        return buildLoginResponse( userDetails, newAccessToken );
    }

    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String rawRefreshToken = extractRefreshToken(request);
        if (rawRefreshToken != null) {
            String tokenHash = hashToken(rawRefreshToken);
            refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(
                    refreshToken -> {
                        refreshToken.setRevoked(true);
                        refreshTokenRepository.save( refreshToken );
                    });
        }
        deleteRefreshCookie(response);
    }

    private LoginResponse buildLoginResponse(
            CustomUserDetails userDetails,
            String token )
    {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(
                authority -> authority != null && authority.startsWith("ROLE_"))
                .toList();

        List<String> permissions = userDetails.getAuthorities()
                .stream() .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority != null && !authority.startsWith("ROLE_"))
                .toList();

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpiration())
                .userId(userDetails.getId().toString())
                .email(userDetails.getEmail())
                .firstname(userDetails.getFirstname())
                .lastname(userDetails.getLastname())
                .userType(userDetails.getUserType())
                .fullName(userDetails.getFullName())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private void saveRefreshToken(
            java.util.UUID userId,
            String rawToken
    ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException( "Utilisateur introuvable" ) );
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(hashToken(rawToken))
                .user(user)
                .expiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
                .revoked(false).createdAt(Instant.now())
                .build();

        refreshTokenRepository.save( refreshToken );
    }

    private String hashToken( String token ) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest( token.getBytes( StandardCharsets.UTF_8 ) );
            return java.util.HexFormat .of() .formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException( "SHA-256 indisponible", e );
        }
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            log.warn("No cookies received");
            return null;
        }

        for (Cookie cookie : cookies) {
            if (REFRESH_COOKIE.equals(cookie.getName())) {
                log.info("Refresh token found");
                return cookie.getValue();
            }
        }
        log.warn("Refresh token not found for URI: {}",
                request.getRequestURI());
        return null;
    }

    private void addRefreshCookie( HttpServletResponse response, String refreshToken ) {
        ResponseCookie cookie = ResponseCookie
                .from(REFRESH_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/api/v1/auth")
                .maxAge( Duration.ofSeconds( refreshTokenExpiration ) )
                .build();
        response.addHeader( HttpHeaders.SET_COOKIE, cookie.toString() );
    }

    private void deleteRefreshCookie(
            HttpServletResponse response
    ) {
        ResponseCookie cookie = ResponseCookie
                .from( REFRESH_COOKIE, "" )
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/api/v1/auth")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader( HttpHeaders.SET_COOKIE, cookie.toString() );
    }
}
