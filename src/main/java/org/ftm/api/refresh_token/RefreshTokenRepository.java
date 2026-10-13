package org.ftm.api.refresh_token;

import org.ftm.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Recherche d'un refresh token à partir
     * de son hash SHA-256.
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash );

    /**
     * Supprime tous les refresh tokens
     * appartenant à un utilisateur.
     */
    void deleteAllByUser(User user);

    /**
     * Révoque tous les refresh tokens d'un utilisateur.
     */
    long countByUserAndRevokedFalse(User user);

    @Modifying
    @Query("""
        UPDATE RefreshToken r
        SET r.revoked = true
        WHERE r.user = :user
          AND r.revoked = false
    """)
    int revokeAllByUser(@Param("user") User user);

    @Query("""
        SELECT rt
        FROM RefreshToken rt
        JOIN FETCH rt.user
        WHERE rt.tokenHash = :tokenHash
        """)
    Optional<RefreshToken> findByTokenHashWithUser(
            @Param("tokenHash") String tokenHash
    );
}
