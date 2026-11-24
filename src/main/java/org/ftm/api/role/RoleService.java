package org.ftm.api.role;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.permission.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public PageResponse<RoleResponse> getRoles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Role> roles = roleRepository.findAll(pageable);
        List<RoleResponse> content = roles.stream()
                .map(RoleMapper::mapToRoleResponse)
                .toList();
        return PageResponse.<RoleResponse>builder()
                .content(content)
                .number(roles.getNumber())
                .size(roles.getSize())
                .totalElements(roles.getTotalElements())
                .totalPages(roles.getTotalPages())
                .first(roles.isFirst())
                .last(roles.isLast())
                .build();
    }

    public RoleResponse getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .map(RoleMapper::mapToRoleResponse)
                .orElseThrow(() -> new EntityNotFoundException("No role found with id: " + roleId));
    }

    public RoleResponse getRoleByName(String name) {
        return roleRepository.findByName(name).map(RoleMapper::mapToRoleResponse)
                .orElseThrow(() -> new EntityNotFoundException("No role found with name: " + name));
    }

    public String createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.name())) {
            throw new RuntimeException("Role name exists");
        }
        final Role role = RoleMapper.mapToCreateRequest(request);
        return roleRepository.save(role).getId().toString();
    }

    public String updateRole(UUID roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("No role found with id: " + roleId));

        if (roleRepository.existsByName(request.name())) {
            throw new RuntimeException("Role name exists");
        }

        role.setName(request.name());
        role.setDescription(request.description());

        return roleRepository.save(role).getId().toString();
    }

    public void deleteRole(UUID roleId) {
        final Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("No role found with id: " + roleId));
        roleRepository.delete(role);
    }

    @Transactional
    public String addPermissionToRole(UUID roleId, UUID permissionId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("No role found with id: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("No permission found with id: " + permissionId));

        boolean exists = role.getRolePermissions().stream()
                .anyMatch(rolePermission -> rolePermission.getPermission().getId().equals(permissionId));

        if (exists) {
            return role.getId().toString();
        }

        RolePermission rolePermission = RolePermission.builder()
                .id(new RolePermissionId(roleId, permissionId))
                .role(role)
                .permission(permission)
                .build();
        role.getRolePermissions().add(rolePermission);

        return roleRepository.save(role).getId().toString();
    }

    @Transactional
    public String removePermissionToRole(UUID roleId, UUID permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("No role found with id: " + roleId));
        role.getRolePermissions().removeIf(rolePermission -> rolePermission.getPermission().getId().equals(permissionId));
        return roleRepository.save(role).getId().toString();
    }

    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll(Sort.by("createdDate").descending());
        return roles.stream()
                .map(RoleMapper::mapToRoleResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> getRolePermissions(UUID roleId) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role not found with id: " + roleId
                ));
        return role.getRolePermissions()
                .stream()
                .map(RolePermission::getPermission)
                .map(PermissionMapper::mapToPermissionResponse)
                .toList();
    }
}
