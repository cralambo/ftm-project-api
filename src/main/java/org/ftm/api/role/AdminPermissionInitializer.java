package org.ftm.api.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ftm.api.permission.Permission;
import org.ftm.api.permission.PermissionRepository;
import org.ftm.api.permission.RolePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminPermissionInitializer {
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public void assignAllPermissions() {

        Role adminRole = roleRepository.findByName(ADMIN_ROLE)
                .orElseThrow(() -> new IllegalStateException(
                        "Role ADMIN not found in database"
                ));

        List<Permission> allPermissions = permissionRepository.findAll();

        int assignedCount = 0;

        for (Permission permission : allPermissions) {

            boolean alreadyAssigned = adminRole.getRolePermissions()
                    .stream()
                    .anyMatch(rp ->
                            rp.getPermission().getId()
                                    .equals(permission.getId())
                    );

            if (!alreadyAssigned) {

                RolePermission rolePermission = new RolePermission();

                rolePermission.setRole(adminRole);
                rolePermission.setPermission(permission);

                adminRole.getRolePermissions().add(rolePermission);

                assignedCount++;
            }
        }

        roleRepository.save(adminRole);

        log.info(
                "ADMIN permissions initialized: {} new permissions assigned, {} total",
                assignedCount,
                adminRole.getRolePermissions().size()
        );
    }
}
