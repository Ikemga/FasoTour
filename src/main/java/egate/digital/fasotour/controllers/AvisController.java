package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.others.AvisRequestDTO;
import egate.digital.fasotour.dto.others.AvisResponseDTO;
import egate.digital.fasotour.services.AvisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/avis")
@RequiredArgsConstructor
public class AvisController {

    private final AvisService avisService;

    // POST /api/fasotour/v1/avis
    @PostMapping
    public ResponseEntity<AvisResponseDTO> createAvis(@RequestBody AvisRequestDTO dto) {
        AvisResponseDTO response = avisService.createAvis(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/fasotour/v1/avis
    @GetMapping
    public ResponseEntity<List<AvisResponseDTO>> getAllAvis() {
        return ResponseEntity.ok(avisService.getAllAvis());
    }

    // GET /api/fasotour/v1/avis/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AvisResponseDTO> getAvisById(@PathVariable Long id) {
        return ResponseEntity.ok(avisService.getAvisById(id));
    }

    // GET //api/fasotour/v1/avis/circuit/{circuitId}
    @GetMapping("/circuit/{circuitId}")
    public ResponseEntity<List<AvisResponseDTO>> getAvisByCircuit(@PathVariable Long circuitId) {
        return ResponseEntity.ok(avisService.getAvisByCircuit(circuitId));
    }

    // GET /api/avis/guide/{guideId}
    @GetMapping("/guide/{guideId}")
    public ResponseEntity<List<AvisResponseDTO>> getAvisByGuide(@PathVariable Long guideId) {
        return ResponseEntity.ok(avisService.getAvisByGuide(guideId));
    }

    // PUT /api/fasotour/v1/avis/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AvisResponseDTO> updateAvis(
            @PathVariable Long id,
            @RequestBody AvisRequestDTO dto
    ) {
        return ResponseEntity.ok(avisService.updateAvis(id, dto));
    }

    // DELETE /api/fasotour/v1/avis/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvis(@PathVariable Long id) {
        avisService.deleteAvis(id);
        return ResponseEntity.noContent().build();
    }
}