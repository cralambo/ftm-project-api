package org.ftm.api.permission;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PageResponse<PermissionResponse> getPermissions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Permission> permissions = permissionRepository.findAll(pageable);
        List<PermissionResponse> content = permissions.stream()
                .map(PermissionMapper::mapToPermissionResponse)
                .toList();
        return PageResponse.<PermissionResponse>builder()
                .content(content)
                .number(permissions.getNumber())
                .size(permissions.getSize())
                .totalElements(permissions.getTotalElements())
                .totalPages(permissions.getTotalPages())
                .first(permissions.isFirst())
                .last(permissions.isLast())
                .build();
    }

    public PermissionResponse getPermissionById(UUID permissionId) {
        return permissionRepository.findById(permissionId)
                .map(PermissionMapper::mapToPermissionResponse)
                .orElseThrow(() -> new EntityNotFoundException("No permission found with id: " + permissionId));
    }

    public PermissionResponse getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .map(PermissionMapper::mapToPermissionResponse)
                .orElseThrow(() -> new EntityNotFoundException("No permission found with name: " + name));
    }

    public String createPermission(PermissionRequest request) {
        if (permissionRepository.existsByName(request.name())) {
            throw new RuntimeException("Permission name exists");
        }
        final Permission permission = PermissionMapper.mapToCreateRequest(request);
        return permissionRepository.save(permission).getId().toString();
    }

    public String updatePermission(UUID permissionId, PermissionRequest request) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("No permission found with id: " + permissionId));
        if (permissionRepository.existsByName(request.name())) {
            throw new RuntimeException("Permission name exists");
        }
        permission.setName(request.name());
        permission.setDescription(request.description());
        return permissionRepository.save(permission).getId().toString();
    }

    public void deletePermission(UUID permissionId) {
        final Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("No permission found with id: " + permissionId));
        permissionRepository.delete(permission);
    }
}
