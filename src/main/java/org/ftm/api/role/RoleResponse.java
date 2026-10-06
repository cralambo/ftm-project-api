package org.ftm.api.role;

import lombok.Builder;

@Builder
public record RoleResponse(
        String id,
        String name,
        String description
) {
}
