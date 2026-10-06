package org.ftm.api.user;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUsers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/update/{user-id}")
    public ResponseEntity<UUID> updateUser(@PathVariable("user-id") UUID userId, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{user-id}/enable")
    public ResponseEntity<UUID> enable(@PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok(userService.enable(userId));
    }

    @PatchMapping("/{user-id}/disable")
    public ResponseEntity<String> disable(@PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok(userService.disable(userId));
    }

    @PostMapping("/{user-id}/roles/{role-id}")
    public ResponseEntity<String> addRoleToUser(
            @PathVariable("user-id") UUID userId,
            @PathVariable("role-id") UUID roleId
    ) {
        return ResponseEntity.ok(userService.addRoleToUser(userId, roleId));
    }

    @DeleteMapping("/{user-id}/roles/{role-id}")
    public ResponseEntity<String> removeRole(
            @PathVariable("user-id") UUID userId,
            @PathVariable("role-id") UUID roleId
    ) {
        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }
}
