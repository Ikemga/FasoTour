package egate.digital.fasotour.security.refreshtoken;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
import egate.digital.fasotour.model.Utilisateur;

@Entity
@Table(name = "refresh_token",
        indexes = {
                @Index(name = "idx_rt_token", columnList = "token", unique = true),
                @Index(name = "idx_rt_family_id", columnList = "family_id"),
                @Index(name = "idx_rt_user_id", columnList = "utilisateur_id")
        })
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String token;

    @Column(name = "family_id", nullable = false, length = 36)
    private String familyId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean revoked = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean used = false;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !revoked && !used && !isExpired();
    }
}