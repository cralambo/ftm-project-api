package org.ftm.api.auth;

import lombok.Builder;

@Builder
public record LoginRequest(
        String email,
        String password
) {
}
