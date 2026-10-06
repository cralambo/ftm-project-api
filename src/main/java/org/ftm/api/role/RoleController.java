package org.ftm.api.role;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<PageResponse<RoleResponse>> getRoles(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(roleService.getRoles(page, size));
    }

    @GetMapping("/{role-id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable("role-id") UUID roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponse> getRoleByName(String name) {
        return ResponseEntity.ok(roleService.getRoleByName(name));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PutMapping("/update/{role-id}")
    public ResponseEntity<String> updateRole(
            @PathVariable("role-id") UUID roleId,
            @RequestBody RoleRequest request
    ) {
        return ResponseEntity.ok(roleService.updateRole(roleId, request));
    }

    @DeleteMapping("/{role-id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("role-id") UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{role-id}/permissions/{permission-id}")
    public ResponseEntity<String> addPermissionToRole(
            @PathVariable("role-id") UUID roleId,
            @PathVariable("permission-id") UUID permissionId
    ) {
        return ResponseEntity.ok(roleService.addPermissionToRole(roleId, permissionId));
    }

    @DeleteMapping("/{role-id}/permissions/{permission-id}")
    public ResponseEntity<String> removePermissionToRole(
            @PathVariable("role-id") UUID roleId,
            @PathVariable("permission-id") UUID permissionId
    ) {
        return ResponseEntity.ok(roleService.removePermissionToRole(roleId, permissionId));
    }
}
