package egate.digital.fasotour.controllers.usermanager;

import egate.digital.fasotour.dto.user.TouristeRequestDTO;
import egate.digital.fasotour.dto.user.TouristeResponseDTO;
import egate.digital.fasotour.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/touristes")
@RequiredArgsConstructor
public class TouristeController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<TouristeResponseDTO>> getAll() {
        return ResponseEntity.ok(authService.getAllTouristes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TouristeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getTouristeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TouristeResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TouristeRequestDTO dto) {
        return ResponseEntity.ok(authService.updateTouriste(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authService.deleteTouriste(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Map<String, String>> toggle(@PathVariable Long id) {
        authService.toggleTouriste(id);
        return ResponseEntity.ok(Map.of("message", "Statut touriste modifié."));
    }
}