package org.ftm.api.permission;

import lombok.Builder;

@Builder
public record PermissionRequest(
        String id,
        String name,
        String description
) {
}
