package egate.digital.fasotour.controllers.usermanager;

import egate.digital.fasotour.dto.user.AgenceRequestDTO;
import egate.digital.fasotour.dto.user.AgenceResponseDTO;
import egate.digital.fasotour.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<AgenceResponseDTO>> getAll() {
        return ResponseEntity.ok(authService.getAllAgences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenceResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getAgenceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenceResponseDTO> update(
            @PathVariable Long id,
            @RequestBody AgenceRequestDTO dto) {
        return ResponseEntity.ok(authService.updateAgence(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authService.deleteAgence(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Map<String, String>> toggle(@PathVariable Long id) {
        authService.toggleAgence(id);
        return ResponseEntity.ok(Map.of("message", "Statut agence modifié."));
    }

    @PatchMapping("/agences/{id}/toggle")
    public ResponseEntity<Map<String, String>> toggleAgence(
            @PathVariable Long id) {
        authService.toggleAgence(id);
        return ResponseEntity.ok(Map.of("message", "Statut agence modifié."));
    }
}