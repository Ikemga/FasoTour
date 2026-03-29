package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.UtilisateurDTO;
import egate.digital.fasotour.model.Agence;
import egate.digital.fasotour.model.Guide;
import egate.digital.fasotour.model.Touriste;
import egate.digital.fasotour.model.Utilisateur;
import egate.digital.fasotour.services.AuthService;
import egate.digital.fasotour.services.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private  final AuthService authService;

    // Tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUsers() {
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }

    // Plus récent
    @GetMapping("/order/date/desc")
    public ResponseEntity<List<UtilisateurDTO>> getUsersRecent() {
        return ResponseEntity.ok(utilisateurService.getUsersByRecent());
    }

    // A → Z
    @GetMapping("/order/name/asc")
    public ResponseEntity<List<UtilisateurDTO>> getUsersAZ() {
        return ResponseEntity.ok(utilisateurService.getUsersByNameAsc());
    }

    // Z → A
    @GetMapping("/order/name/desc")
    public ResponseEntity<List<UtilisateurDTO>> getUsersZA() {
        return ResponseEntity.ok(utilisateurService.getUsersByNameDesc());
    }

    // Compter par rôle
    @GetMapping("/count/{role}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(utilisateurService.countUsersByRole(role));
    }

    // Nombre total d'utilisateurs
    @GetMapping("/count")
    public ResponseEntity<Long> getAllUtilisateur() {
        return ResponseEntity.ok(utilisateurService.getCountAllUser());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UtilisateurDTO>> searchUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean actif
    ) {
        return ResponseEntity.ok(utilisateurService.searchUsers(search, actif));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        try {
            // 1. Récupérer l'utilisateur pour détecter son type
            Utilisateur utilisateur = utilisateurService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + id));

            // 2. Appeler le bon service selon le type
            if (utilisateur instanceof Guide) {
                authService.deleteGuide(id);

            } else if (utilisateur instanceof Agence) {
                authService.deleteAgence(id);

            } else if (utilisateur instanceof Touriste) {
                authService.deleteTouriste(id);

            } else {
                utilisateurService.remove(id); // Utilisateur simple sans relations
            }

            return ResponseEntity.ok()
                    .body(Map.of("message", "Utilisateur supprimé avec succès"));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la suppression"));
        }
    }
}