package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.refreshtokendto.RefreshTokenRequestDTO;
import egate.digital.fasotour.dto.refreshtokendto.RefreshTokenResponseDTO;
import egate.digital.fasotour.dto.site.CategorieRequestDTO;
import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import egate.digital.fasotour.dto.site.SiteTouristiqueRequestDTO;
import egate.digital.fasotour.dto.site.SiteTouristiqueResponseDTO;
import egate.digital.fasotour.model.Role;
import egate.digital.fasotour.model.Touriste;
import egate.digital.fasotour.model.Utilisateur;
import egate.digital.fasotour.repository.RoleRepository;
import egate.digital.fasotour.repository.UtilisateurRepository;
import egate.digital.fasotour.security.jwt.JwtService;
import egate.digital.fasotour.security.refreshtoken.RefreshToken;
import egate.digital.fasotour.security.refreshtoken.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.dto.auth.AuthResponseDTO;
import egate.digital.fasotour.dto.auth.LoginRequestDTO;
import egate.digital.fasotour.dto.user.*;
import egate.digital.fasotour.services.AuthService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private  UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    /**
     * Inscription
     * */
// ------------------------------------------------------------------------ Touriste
    //Touriste
    @PostMapping("/inscription/touriste")
    public ResponseEntity<Map<String, String>> inscrireTouriste(
            @RequestBody TouristeRequestDTO dto) {
        authService.inscrireTouriste(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Inscription réussie ! Vérifiez votre mail."));
    }

    @PostMapping("/inscription/touriste/batch")
    public ResponseEntity<List<Map<String, String>>> createTouristeBatch(
            @RequestBody List<TouristeRequestDTO> dtos) {
        dtos.forEach(authService::inscrireTouriste);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(List.of(Map.of("message", "Touristes inscrits avec succès !")));
    }
// ------------------------------------------------------------------------ Touriste


    //Guide
    @PostMapping("/inscription/guide")
    public ResponseEntity<Map<String, String>> inscrireGuide(
            @RequestBody GuideRequestDTO dto) {
        authService.inscrireGuide(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Inscription réussie ! Vérifiez votre mail."));
    }

    @PostMapping("/inscription/guide/batch")
    public ResponseEntity<List<Map<String, String>>> createGuideBatch(
            @RequestBody List<GuideRequestDTO> dtos) {
        dtos.forEach(authService::inscrireGuide);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(List.of(Map.of("message", "Guides inscrits avec succès !")));
    }
    //---------------------------------------------------------------------------------------------------------------
    //Agence
    @PostMapping("/inscription/agence")
    public ResponseEntity<Map<String, String>> inscrireAgence(
            @RequestBody AgenceRequestDTO dto) {
        authService.inscrireAgence(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Inscription réussie ! Vérifiez votre mail."));
    }

    @PostMapping("/inscription/agence/batch")
    public ResponseEntity<List<Map<String, String>>> createAgenceBatch(
            @RequestBody List<AgenceRequestDTO> dtos) {
        dtos.forEach(authService::inscrireAgence);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(List.of(Map.of("message", "Agences inscrites avec succès !")));
    }
    //---------------------------------------------------------------------------------------------------------------

    /**
     * Activation
     * */

    //activation
    @PostMapping("/activation")
    public ResponseEntity<Map<String, String>> activer(@RequestBody Map<String, String> body) {
        authService.activer(body.get("code"));
        return ResponseEntity.ok(Map.of("message", "Compte activé avec succès !"));
    }

    @PostMapping("/renvoi_code")
    public ResponseEntity<Map<String, String>> renvoiCode(@RequestBody Map<String, String> body) {
        authService.renvoyerCode(body.get("mail"));
        return ResponseEntity.ok(Map.of("message", "Nouveau code envoyé sur votre mail."));
    }

    /**
     * Connexion
     * */

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO dto,
            HttpServletRequest request) {

        AuthResponseDTO response = authService.connecter(
                dto,
                getClientIp(request),
                request.getHeader("User-Agent")
        );

        return ResponseEntity.ok(response);
    }
    // Refresh Token

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refresh(
            @RequestBody RefreshTokenRequestDTO dto,
            HttpServletRequest request) {

        RefreshToken newRefreshToken = refreshTokenService.rotate(
                dto.refreshToken(),
                getClientIp(request),
                request.getHeader("User-Agent")
        );

        String newAccessToken = jwtService.generateToken(newRefreshToken.getUtilisateur());

        return ResponseEntity.ok(new RefreshTokenResponseDTO(
                newAccessToken,
                newRefreshToken.getToken()
        ));
    }

    //Déconnexion

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequestDTO dto) {
        refreshTokenService.revokeFamily(dto.refreshToken());
        return ResponseEntity.ok("Déconnexion réussie.");
    }

    @PostMapping("/logout_all")
    public ResponseEntity<String> logoutAll(
            @org.springframework.security.core.annotation.AuthenticationPrincipal
            Utilisateur utilisateur) {
        refreshTokenService.revokeAllForUser(utilisateur.getId());
        return ResponseEntity.ok("Toutes vos sessions ont été fermées.");
    }

    @PostMapping("/init-admin")
    @Transactional
    public ResponseEntity<Map<String, String>> initAdmin() {

        if (utilisateurRepository.findByMail("admin@fasotour.bf").isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Admin déjà existant !"));
        }

        Role roleAdmin = roleRepository.findByRoles("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoles("ADMIN");
                    r.setDescription("Administrateur principal de FasoTour");
                    r.setStatut("ACTIVE");
                    return roleRepository.save(r);
                });

        Touriste admin = new Touriste();
        admin.setNomComplet("Administrateur FasoTour");
        admin.setMail("admin@fasotour.bf");
        admin.setMotDePasse(passwordEncoder.encode("Admin@2024!"));
        admin.setTelephone("+226 67 54 32 91");
        admin.setAdresse("Ouagadougou, Burkina Faso");
        admin.setPays("Burkina Faso");
        admin.setBio("Administrateur principal de la plateforme FasoTour.");
        admin.setActif(true);
        admin.setRoles(Set.of(roleAdmin));

        utilisateurRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Admin créé avec succès !",
                "mail",    "admin@fasotour.bf",
                "motDePasse", "Admin@2024!"
        ));
    }

    //Helps
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        return ip;
    }

}