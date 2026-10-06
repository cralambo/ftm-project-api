package org.ftm.api.auth;

import lombok.Builder;

@Builder
public record RegisterRequest(
        String email,
        String firstname,
        String lastname,
        String user_type
) {
}
