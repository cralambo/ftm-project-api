package org.ftm.api.permission;

import lombok.Builder;

@Builder
public record PermissionResponse(
        String id,
        String name,
        String description
) {
}
