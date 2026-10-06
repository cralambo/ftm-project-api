package org.ftm.api.role;

public class RoleMapper {

    public static RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId().toString())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    public static Role mapToCreateRequest(RoleRequest request) {
        return Role.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }
}
