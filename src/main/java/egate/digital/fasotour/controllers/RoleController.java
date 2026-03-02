package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.CategorieRequestDTO;
import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.dto.user.RoleRequestDTO;
import egate.digital.fasotour.dto.user.RoleResponseDTO;
import egate.digital.fasotour.services.RolesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RolesService roleService;

    //
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }

    //
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @GetMapping("role/{r}")
    public ResponseEntity<RoleResponseDTO> getByRole(@PathVariable String role) {
        return ResponseEntity.ok(roleService.findByRole(role));
    }

    //
    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(
            @RequestBody RoleRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleService.saveRole(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<RoleResponseDTO>> createBatch(
            @RequestBody List<RoleRequestDTO> dtos) {
        List<RoleResponseDTO> result = dtos.stream()
                .map(roleService::saveRole)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(
            @PathVariable Long id,
            @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.ok(roleService.updateRole(id, dto));
    }

    //
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    //
    @PatchMapping("/{id}/statut")
    public ResponseEntity<RoleResponseDTO> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.Statut(id));
    }
}