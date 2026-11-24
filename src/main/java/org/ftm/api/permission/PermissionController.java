package org.ftm.api.permission;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('permissions.view')")
    public ResponseEntity<PageResponse<PermissionResponse>> getPermissions(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(permissionService.getPermissions(page, size));
    }

    @GetMapping("/{permission-id}")
    @PreAuthorize("hasAuthority('permissions.view')")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable("permission-id") UUID permissionId) {
        return ResponseEntity.ok(permissionService.getPermissionById(permissionId));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('permissions.view')")
    public ResponseEntity<PermissionResponse> getPermissionByName(@PathVariable String name) {
        return ResponseEntity.ok(permissionService.getPermissionByName(name));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('permissions.create')")
    public ResponseEntity<String> createPermission(@RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.createPermission(request));
    }

    @PutMapping("/update/{permission-id}")
    @PreAuthorize("hasAuthority('permissions.update')")
    public ResponseEntity<String> updatePermission(
            @PathVariable("permission-id") UUID permissionId, @RequestBody PermissionRequest request
    ) {
        return ResponseEntity.ok(permissionService.updatePermission(permissionId, request));
    }

    @DeleteMapping("/{permission-id}")
    @PreAuthorize("hasAuthority('permissions.delete')")
    public ResponseEntity<Void> deletePermission(@PathVariable("permission-id") UUID permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('permissions.view')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }
}
