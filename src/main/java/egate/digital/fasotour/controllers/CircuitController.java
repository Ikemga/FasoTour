package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.CircuitRequestDTO;
import egate.digital.fasotour.dto.site.CircuitResponseDTO;
import egate.digital.fasotour.services.CircuitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/circuits")
@RequiredArgsConstructor
public class CircuitController {

    private final CircuitService circuitService;

    @GetMapping
    public ResponseEntity<List<CircuitResponseDTO>> getAllCircuits() {
        return ResponseEntity.ok(circuitService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CircuitResponseDTO> getCircuitById(@PathVariable Long id) {
        return ResponseEntity.ok(circuitService.getById(id));
    }

    @GetMapping("/name/{circuitName}")
    public ResponseEntity<CircuitResponseDTO> getCircuitByName(@PathVariable String circuitName) {
        return ResponseEntity.ok(circuitService.getByCircuit(circuitName));
    }

    @PostMapping
    public ResponseEntity<CircuitResponseDTO> createCircuit(@Valid @RequestBody CircuitRequestDTO dto) {
        CircuitResponseDTO created = circuitService.createCircuit(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<CircuitResponseDTO>> createBatch(@RequestBody List<CircuitRequestDTO> dtos) {
        List<CircuitRequestDTO> copyDtos = new ArrayList<>(dtos);

        List<CircuitResponseDTO> result = copyDtos.stream()
                .map(circuitService::createCircuit)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CircuitResponseDTO> updateCircuit(@PathVariable Long id, @Valid @RequestBody CircuitRequestDTO dto) {
        CircuitResponseDTO updated = circuitService.updatcircuite(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuit(@PathVariable Long id) {
        circuitService.remov(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<CircuitResponseDTO>> getCircuitsByAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(circuitService.getByAgence(agenceId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CircuitResponseDTO>> getCircuitsByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(circuitService.getByStatut(statut));
    }
}