package egate.digital.fasotour.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.dto.paie.*;
import egate.digital.fasotour.services.PaiementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @PostMapping
    public ResponseEntity<PaiementResponseDTO> create(@RequestBody PaiementRequestDTO dto) {
        return new ResponseEntity<>(paiementService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaiementResponseDTO> update(@PathVariable Long id,
                                                      @RequestBody PaiementRequestDTO dto) {
        return ResponseEntity.ok(paiementService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaiementResponseDTO>> getAll() {
        return ResponseEntity.ok(paiementService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paiementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}