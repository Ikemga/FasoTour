package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.SiteTouristiqueResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.dto.site.CategorieRequestDTO;
import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import egate.digital.fasotour.services.CategorieService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    // GET /api/v1/categories
    @GetMapping("/all")
    public ResponseEntity<List<CategorieResponseDTO>> getAll() {
        return ResponseEntity.ok(categorieService.getAllCategorie());
    }


    // GET /api/v1/sites?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<CategorieResponseDTO>> getAllByPage(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(categorieService.findAll(pageable));
    }

    // GET /api/v1/categories/{id}
    @GetMapping("{id}")
    public ResponseEntity<CategorieResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.getCategorieById(id));
    }

    // GET /api/v1/categories/nom/{categorie}
    @GetMapping("/nom/{categorie}")
    public ResponseEntity<CategorieResponseDTO> getByNom(@PathVariable String categorie) {
        return ResponseEntity.ok(categorieService.getByNom(categorie));
    }

    // GET /api/v1/categories/newest
    @GetMapping("/recent")
    public ResponseEntity<List<CategorieResponseDTO>> getByRecentAdd() {
        return ResponseEntity.ok(categorieService.getAllCategorieNewestFirst());
    }
    // GET /api/v1/categories/alphabetique
    @GetMapping("/alphabetique")
    public ResponseEntity<List<CategorieResponseDTO>> getAlphabetical() {
        return ResponseEntity.ok(categorieService.getAllCategorieAlphabetical());
    }

    // POST /api/v1/categories
    @PostMapping
    public ResponseEntity<CategorieResponseDTO> create(@RequestBody CategorieRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categorieService.saveCategorie(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<CategorieResponseDTO>> createBatch(
            @RequestBody List<CategorieRequestDTO> dtos) {
        List<CategorieResponseDTO> result = dtos.stream()
                .map(categorieService::saveCategorie)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // PUT /api/v1/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategorieResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CategorieRequestDTO dto) {
        return ResponseEntity.ok(categorieService.updateCategorie(id, dto));
    }

    // DELETE /api/v1/categories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }
}