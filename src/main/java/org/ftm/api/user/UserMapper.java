package org.ftm.api.user;

import org.ftm.api.permission.PermissionMapper;
import org.ftm.api.permission.PermissionResponse;
import org.ftm.api.permission.RolePermission;
import org.ftm.api.role.RoleMapper;
import org.ftm.api.role.RoleResponse;
import org.ftm.api.role.UserRole;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .user_type(user.getUserType().toString())
                .enabled(user.isEnabled())
                .build();
    }

    public static User mapToCreateRequest(UserRequest request) {
        return User.builder()
                .email(request.email())
                .password(request.confirm_password())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .build();
    }

    public static UserDetailResponse mapToUserDetailResponse(User user) {

        Set<RoleResponse> roles = user.getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(RoleMapper::mapToRoleResponse)
                .collect(Collectors.toSet());

        Set<PermissionResponse> permissions = user.getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .flatMap(role -> role.getRolePermissions().stream())
                .filter(Objects::nonNull)
                .map(RolePermission::getPermission)
                .filter(Objects::nonNull)
                .map(PermissionMapper::mapToPermissionResponse)
                .collect(Collectors.toSet());

        return UserDetailResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .userType(user.getUserType().name())
                .enabled(user.isEnabled())
                .roles(roles)
                .permissions(permissions)
                .build();
    }
}