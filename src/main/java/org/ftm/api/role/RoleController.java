package org.ftm.api.role;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.permission.PermissionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<PageResponse<RoleResponse>> getRoles(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(roleService.getRoles(page, size));
    }

    @GetMapping("/{role-id}")
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable("role-id") UUID roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getRoleByName(name));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('roles.create')")
    public ResponseEntity<String> createRole(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PutMapping("/update/{role-id}")
    @PreAuthorize("hasAuthority('roles.update')")
    public ResponseEntity<String> updateRole(
            @PathVariable("role-id") UUID roleId,
            @RequestBody RoleRequest request
    ) {
        return ResponseEntity.ok(roleService.updateRole(roleId, request));
    }

    @DeleteMapping("/{role-id}")
    @PreAuthorize("hasAuthority('roles.delete')")
    public ResponseEntity<Void> deleteRole(@PathVariable("role-id") UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{role-id}/permissions/{permission-id}")
    @PreAuthorize("hasAuthority('roles.permissions.add')")
    public ResponseEntity<String> addPermissionToRole(
            @PathVariable("role-id") UUID roleId,
            @PathVariable("permission-id") UUID permissionId
    ) {
        return ResponseEntity.ok(roleService.addPermissionToRole(roleId, permissionId));
    }

    @DeleteMapping("/{role-id}/permissions/{permission-id}")
    @PreAuthorize("hasAuthority('roles.permissions.remove')")
    public ResponseEntity<String> removePermissionToRole(
            @PathVariable("role-id") UUID roleId,
            @PathVariable("permission-id") UUID permissionId
    ) {
        return ResponseEntity.ok(roleService.removePermissionToRole(roleId, permissionId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('roles.view')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{role-id}/permissions")
    @PreAuthorize("hasAuthority('roles.permissions.view')")
    public ResponseEntity<List<PermissionResponse>> getRolePermissions(
            @PathVariable("role-id") UUID roleId
    ) {
        return ResponseEntity.ok(roleService.getRolePermissions(roleId));
    }
}
