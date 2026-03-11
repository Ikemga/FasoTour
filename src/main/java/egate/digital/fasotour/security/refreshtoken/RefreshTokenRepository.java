package egate.digital.fasotour.security.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // Révoque TOUTE la famille → appelé quand une réutilisation est détectée.
    // Force la reconnexion sur tous les appareils de la session compromise.
    @Modifying
    @Transactional 
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.familyId = :familyId")
    void revokeAllByFamilyId(@Param("familyId") String familyId);

    // Révoque tous les tokens actifs d'un utilisateur.
    // Utilisé au logout global (déconnexion de tous les appareils).
    @Modifying
    @Transactional 
    @Query("UPDATE RefreshToken r SET r.revoked = true " +
           "WHERE r.utilisateur.id = :userId AND r.revoked = false")
    void revokeAllByUserId(@Param("userId") Long userId);

    // Supprime les tokens expirés ou révoqués depuis plus de 24h.
    // Appelé par le @Scheduled dans RefreshTokenService.
    @Modifying
    @Transactional  
    @Query("DELETE FROM RefreshToken r " +
           "WHERE r.expiresAt < :threshold OR " +
           "(r.revoked = true AND r.createdAt < :threshold)")
    void deleteExpiredAndRevoked(@Param("threshold") Instant threshold);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.utilisateur.id = :userId")
    void deleteByUtilisateurId(@Param("userId") Long userId);
}