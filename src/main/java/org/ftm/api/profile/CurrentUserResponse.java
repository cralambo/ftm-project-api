package org.ftm.api.profile;

import lombok.Builder;

import java.util.List;

@Builder
public record CurrentUserResponse(
        String id,
        String email,
        String firstname,
        String lastname,
        boolean enabled,
        List<String> roles,
        String userType,
        Object profile
) {
}
