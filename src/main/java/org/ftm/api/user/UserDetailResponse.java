package org.ftm.api.user;

import lombok.Builder;
import org.ftm.api.permission.PermissionResponse;
import org.ftm.api.role.RoleResponse;

import java.util.Collection;

@Builder
public record UserDetailResponse(
        String id,
        String email,
        String firstname,
        String lastname,
        String userType,
        boolean enabled,
        Collection<RoleResponse> roles,
        Collection<PermissionResponse> permissions
) {
}
