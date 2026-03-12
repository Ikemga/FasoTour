package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.CircuitRequestDTO;
import egate.digital.fasotour.dto.site.CircuitResponseDTO;
import egate.digital.fasotour.dto.site.SiteTouristiqueResponseDTO;
import egate.digital.fasotour.services.CircuitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/circuits")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
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


    // Plus récent en premier
    @GetMapping("/order/date/desc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByDateDesc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByCreatedAtDesc());
    }

    // Plus ancien en premier
    @GetMapping("/order/date/asc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByDateAsc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByCreatedAtAsc());
    }

    // Alphabétique A → Z
    @GetMapping("/order/name/asc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByNameAsc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByNameAsc());
    }

    // Alphabétique Z → A
    @GetMapping("/order/name/desc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByNameDesc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByNameDesc());
    }

    // GET /api/v1/circuits?page=0&size=10
    @GetMapping("/page")
    public ResponseEntity<Page<CircuitResponseDTO>> getAllPage(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(circuitService.findPage(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CircuitResponseDTO>> searchByName(
            @RequestParam(defaultValue = "") String name) {
        return ResponseEntity.ok(circuitService.searchByName(name));
    }

    @GetMapping("/name/{circuitName}")
    public ResponseEntity<CircuitResponseDTO> getCircuitByName(@PathVariable String circuitName) {
        return ResponseEntity.ok(circuitService.getByCircuit(circuitName));
    }

    /*
    @PostMapping
    public ResponseEntity<CircuitResponseDTO> create(@Valid @RequestBody CircuitRequestDTO dto) {
        CircuitResponseDTO created = circuitService.createCircuit(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    */
    @PostMapping
    public ResponseEntity<CircuitResponseDTO> createCircuit(
            @Valid @RequestBody CircuitRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(circuitService.createCircuit(dto));
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