package org.ftm.api.auth;

import lombok.Builder;

import java.util.Collection;

@Builder
public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        String userId,
        String email,
        String firstname,
        String lastname,
        String userType,
        String fullName,
        Collection<String> roles,
        Collection<String> permissions
) {
}
