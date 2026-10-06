package org.ftm.api.user;

import lombok.Builder;

@Builder
public record UserRequest(
        String id,
        String email,
        String password,
        String confirm_password
) {
}
