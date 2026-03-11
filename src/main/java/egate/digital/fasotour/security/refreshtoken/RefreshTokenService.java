

/*
package egate.digital.fasotour.security.refreshtoken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.model.Utilisateur;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    private  final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-token-expiration-ms:604800000}") // 7 jours par défaut
    private long refreshTokenExpirationMs;

    // Create
    @Transactional
    public RefreshToken createTokenForNewSession(Utilisateur user, String ipAdresse, String userAgent){
        String newFamilyId = UUID.randomUUID().toString();
        return  buildAndSave(user, newFamilyId,ipAdresse, userAgent);
    }

    // Rotation
    @Transactional
    public  RefreshToken rotation(String rawToken, String ipAdresse, String userAgent){
        RefreshToken token = refreshTokenRepository.findByToken(rawToken)
                .orElseThrow(()->new TokenException(
                        "REFRESH_TOKEN_NOT_FOUND", "Refresh token introuvable. Veuillez vous reconnecter."));

        // 1.
        if (token.isUsed()){
            log.warn("[SECURITY] Réutilisation de token détectée ! " +
                            "familyId={} userId={} ip={}",
                    token.getFamilyId(), token.getUtilisateur().getId(), ipAdresse);

            refreshTokenRepository.revokeAllByFamilyId(token.getFamilyId());

            throw new TokenException(
                    "TOKEN_REUSE_DETECTED",
                    "Session compromise détectée. Toutes vos sessions ont été fermées. " +
                            "Veuillez vous reconnecter.");
        }

        //2.
        if (token.isRevoked()) {
            throw new TokenException("TOKEN_REVOKED",
                    "Ce token a été révoqué. Veuillez vous reconnecter.");
        }

        //3.
        if (token.isExpired()) {
            throw new TokenException("TOKEN_EXPIRED",
                    "Votre session a expiré. Veuillez vous reconnecter.");
        }

        //4.
        token.setUsed(true);
        refreshTokenRepository.save(token);
        //5.
        return buildAndSave(token.getUtilisateur(), token.getFamilyId(), ipAdresse, userAgent);
    }

    //
    @Transactional
    public void revokeFamily(String familyId) {
        refreshTokenRepository.revokeAllByFamilyId(familyId);
        log.info("Famille de tokens révoquée : {}", familyId);
    }

    //
    @Transactional
    public void revokeAllForUser(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("Tous les tokens révoqués pour l'utilisateur : {}", userId);
    }

    //Supression automatique de token expiré chaque 1h
    @Scheduled(fixedDelay = 3_600_000)
    @Transactional
    public void cleanupExpiredTokens() {
        // On garde 24h pour l'audit avant suppression définitive
        Instant threshold = Instant.now().minusSeconds(86_400);
        refreshTokenRepository.deleteExpiredAndRevoked(threshold);
        log.debug("Nettoyage des refresh tokens expirés effectué.");
    }

    //Privé
    private RefreshToken buildAndSave(Utilisateur user, String familyId,
                                      String ipAddress, String userAgent) {
        // Supprimer tous les tokens existants pour cet utilisateur
        refreshTokenRepository.deleteByUtilisateurId(user.getId());

        RefreshToken newToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .familyId(familyId)
                .utilisateur(user)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .used(false)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        return refreshTokenRepository.save(newToken);
    }*/
package egate.digital.fasotour.security.refreshtoken;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import egate.digital.fasotour.model.Utilisateur;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final EntityManager entityManager;
    private final long refreshTokenExpirationMs = 604_800_000; // 7 jours

    @Transactional
    public RefreshToken createTokenForNewSession(Utilisateur user, String ip, String userAgent) {
        // Supprime l’ancien token
        refreshTokenRepository.deleteByUtilisateurId(user.getId());
        refreshTokenRepository.flush(); // ← Forcer le DELETE avant l'INSERT
        entityManager.clear();

        RefreshToken newToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .familyId(UUID.randomUUID().toString())
                .utilisateur(user)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .used(false)
                .ipAddress(ip)
                .userAgent(userAgent)
                .build();

        return refreshTokenRepository.save(newToken);
    }

    @Transactional
    public RefreshToken rotate(String rawToken, String ip, String userAgent) {
        RefreshToken token = refreshTokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new TokenException("REFRESH_TOKEN_NOT_FOUND", "Token introuvable."));

        if (token.isUsed()) {
            log.warn("[SECURITY] Réutilisation de token détectée ! familyId={} userId={}",
                    token.getFamilyId(), token.getUtilisateur().getId());
            refreshTokenRepository.revokeAllByFamilyId(token.getFamilyId());
            throw new TokenException("TOKEN_REUSE_DETECTED",
                    "Session compromise détectée. Toutes vos sessions ont été fermées.");
        }

        if (token.isRevoked() || token.isExpired()) {
            throw new TokenException("TOKEN_INVALID", "Token invalide. Veuillez vous reconnecter.");
        }

        token.setUsed(true);
        refreshTokenRepository.save(token);
        refreshTokenRepository.flush();
        //entityManager.clear();

        // Nouveau token avec le même familyId
        RefreshToken newToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .familyId(token.getFamilyId())
                .utilisateur(token.getUtilisateur())
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .used(false)
                .ipAddress(ip)
                .userAgent(userAgent)
                .build();

        return refreshTokenRepository.save(newToken);
    }

    @Transactional
    public void revokeFamily(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenException("REFRESH_TOKEN_NOT_FOUND", "Token introuvable."));
        refreshTokenRepository.revokeAllByFamilyId(token.getFamilyId());
        log.info("Famille de tokens révoquée : {}", token.getFamilyId());
    }

    @Transactional
    public void revokeAllForUser(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("Tous les tokens révoqués pour l'utilisateur : {}", userId);
    }

    @Scheduled(fixedDelay = 3_600_000)
    @Transactional
    public void cleanupExpired() {
        Instant threshold = Instant.now().minusSeconds(86_400);
        refreshTokenRepository.deleteExpiredAndRevoked(threshold);
        log.debug("Refresh tokens expirés nettoyés.");
    }
}