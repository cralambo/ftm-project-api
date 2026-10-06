package org.ftm.api.permission;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<PageResponse<PermissionResponse>> getPermissions(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(permissionService.getPermissions(page, size));
    }

    @GetMapping("/{permission-id}")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable("permission-id") UUID permissionId) {
        return ResponseEntity.ok(permissionService.getPermissionById(permissionId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PermissionResponse> getPermissionByName(@PathVariable String name) {
        return ResponseEntity.ok(permissionService.getPermissionByName(name));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPermission(@RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.createPermission(request));
    }

    @PutMapping("/update/{permission-id}")
    public ResponseEntity<String> updatePermission(
            @PathVariable("permission-id") UUID permissionId, @RequestBody PermissionRequest request
    ) {
        return ResponseEntity.ok(permissionService.updatePermission(permissionId, request));
    }

    @DeleteMapping("/{permission-id}")
    public ResponseEntity<Void> deletePermission(@PathVariable("permission-id") UUID permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}
