package org.ftm.api.refresh_token;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.user.User;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(
                        name = "idx_refresh_tokens_token_hash",
                        columnList = "token_hash" ),
                @Index(
                        name = "idx_refresh_tokens_user_id",
                        columnList = "user_id" ) })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column( name = "token_hash", nullable = false, unique = true, length = 64 )
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_refresh_tokens_user"
            )
    )
    private User user;

    @Column( name = "expires_at", nullable = false )
    private Instant expiresAt;

    @Column( name = "revoked", nullable = false ) @Builder.Default
    private boolean revoked = false;

    @Column( name = "created_at", nullable = false, updatable = false )
    @Builder.Default
    private Instant createdAt = Instant.now();
}
