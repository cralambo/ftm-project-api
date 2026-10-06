package org.ftm.api.role;

import lombok.Builder;

@Builder
public record RoleRequest(
        String id,
        String name,
        String description
) {
}