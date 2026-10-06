package org.ftm.api.user;

import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String email,
        String user_type,
        String firstname,
        String lastname,
        boolean enabled
) {
}
