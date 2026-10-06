package org.ftm.api.permission;

public class PermissionMapper {

    public static PermissionResponse mapToPermissionResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId().toString())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

    public static Permission mapToCreateRequest(PermissionRequest request) {
        return Permission.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }
}
