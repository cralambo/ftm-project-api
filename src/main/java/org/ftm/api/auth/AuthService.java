package org.ftm.api.auth;

import lombok.RequiredArgsConstructor;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.security.JwtService;
import org.ftm.api.user.User;
import org.ftm.api.user.UserAccountService;
import org.ftm.api.user.UserType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountService userAccountService;

    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        assert userDetails != null;
        String token = jwtService.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> {
                    assert authority != null;
                    return authority.startsWith("ROLE_");
                })
                .toList();

        List<String> permissions = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> {
                    assert authority != null;
                    return !authority.startsWith("ROLE_");
                }) .toList();

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
}
