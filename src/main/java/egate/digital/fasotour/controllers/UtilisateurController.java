package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.UtilisateurDTO;
import egate.digital.fasotour.services.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

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
}