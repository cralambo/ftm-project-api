package org.ftm.api.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.role.Role;
import org.ftm.api.role.RoleRepository;
import org.ftm.api.role.UserRole;
import org.ftm.api.role.UserRoleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public PageResponse<UserResponse> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponse> content = users.stream()
                .map(UserMapper::mapToUserResponse)
                .toList();
        return PageResponse.<UserResponse>builder()
                .content(content)
                .number(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .first(users.isFirst())
                .last(users.isLast())
                .build();
    }

    public UserResponse getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapToUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
    }

    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::mapToUserResponse)
                .orElseThrow();
    }

    public UUID createUser(UserRequest request) {
        User user = UserMapper.mapToCreateRequest(request);
        return userRepository.save(user).getId();
    }

    public UUID updateUser(UUID userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
        // todo
        return null;
    }

    public void deleteUser(UUID userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
        userRepository.delete(user);
    }

    public UUID enable(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
        user.setEnabled(true);
        return userRepository.save(user).getId();
    }

    public String disable(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
        user.setEnabled(false);
        return userRepository.save(user).getId().toString();
    }

    public String addRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("No role found with id: " + roleId));

        boolean exists = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRole().getId().equals(roleId));

        if (exists) {
            return userId.toString();
        }

        UserRole userRole = UserRole.builder()
                .id(new UserRoleId(userId, roleId))
                .user(user)
                .role(role)
                .build();
        user.getUserRoles().add(userRole);
        return userRepository.save(user).getId().toString();
    }

    public String removeRole(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
        user.getUserRoles().removeIf(userRole -> userRole.getRole().getId().equals(roleId));
        return userRepository.save(user).getId().toString();
    }
}
