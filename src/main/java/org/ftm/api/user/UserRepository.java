package org.ftm.api.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.userRoles ur
        LEFT JOIN FETCH ur.role r
        LEFT JOIN FETCH r.rolePermissions rp
        LEFT JOIN FETCH rp.permission
        WHERE u.email = :email
    """)
    Optional<User> findByEmailWithAuthorities(@Param("email") String email);

    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findByIdWithRoles(@Param("userId") UUID userId);

    @Query("SELECT u FROM User u")
    Page<User> findAllUsers(Pageable pageable);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.userRoles ur
        LEFT JOIN FETCH ur.role r
        LEFT JOIN FETCH r.rolePermissions rp
        LEFT JOIN FETCH rp.permission
        WHERE u.id IN :ids
    """)
    List<User> findAllUserWithAuthorities(@Param("ids") Collection<UUID> ids);
}
