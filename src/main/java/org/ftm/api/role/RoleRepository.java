package org.ftm.api.role;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);

    @EntityGraph(attributePaths = {
            "rolePermissions",
            "rolePermissions.permission"
    })
    @Query("SELECT r FROM Role r WHERE r.id = :roleId")
    Optional<Role> findByIdWithPermissions(@Param("roleId") UUID roleId);
}
