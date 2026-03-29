package egate.digital.fasotour.controllers.usermanager;

import egate.digital.fasotour.dto.user.GuideRequestDTO;
import egate.digital.fasotour.dto.user.GuideResponseDTO;
import egate.digital.fasotour.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/guides")
@RequiredArgsConstructor
public class GuideController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<GuideResponseDTO>> getAll() {
        return ResponseEntity.ok(authService.getAllGuides());
    }

    @GetMapping("/order/name/asc")
    public ResponseEntity<List<GuideResponseDTO>> getAllOrderAlp() {
        return ResponseEntity.ok(authService.getAllGuidesorderAlp());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getGuideById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideResponseDTO> update(
            @PathVariable Long id,
            @RequestBody GuideRequestDTO dto) {
        return ResponseEntity.ok(authService.updateGuide(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        authService.deleteGuide(id);
        return ResponseEntity.ok(Map.of("message", "Guide supprimé avec succès"));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Map<String, String>> toggle(@PathVariable Long id) {
        authService.toggleGuide(id);
        return ResponseEntity.ok(Map.of("message", "Statut guide modifié."));
    }
}