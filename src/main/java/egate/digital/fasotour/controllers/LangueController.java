package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.model.Langue;
import egate.digital.fasotour.services.LanguesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/langues")
@RequiredArgsConstructor
public class LangueController {

    private final LanguesService languesService;

    // GET /api/v1/langues
    @GetMapping
    public ResponseEntity<List<Langue>> getAll() {
        return ResponseEntity.ok(languesService.getAllLangues());
    }

    // GET /api/v1/langues/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Langue> getById(@PathVariable Long id) {
        return ResponseEntity.ok(languesService.getLangueById(id));
    }

    // GET /api/v1/langues/nom/{langues}
    @GetMapping("/nom/{langues}")
    public ResponseEntity<Langue> getByNom(@PathVariable String langues) {
        return ResponseEntity.ok(languesService.getByLangues(langues));
    }

    // GET /api/v1/langues/code/{code}
    @GetMapping("/code/{code}")
    public ResponseEntity<Langue> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(languesService.getByCode(code));
    }

    // POST /api/v1/langues
    @PostMapping
    public ResponseEntity<Langue> create(@RequestBody Langue langue) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(languesService.saveLangue(langue));
    }

    // PUT /api/v1/langues/batch
    @PostMapping("/batch")
    public ResponseEntity<List<Langue>> createBatch(
            @RequestBody List<Langue> langues) {
        List<Langue> result = langues.stream()
                .map(languesService::saveLangue)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // PUT /api/v1/langues/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Langue> update(
            @PathVariable Long id,
            @RequestBody Langue langue) {
        return ResponseEntity.ok(languesService.updateLangue(id, langue));
    }

    // DELETE /api/v1/langues/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languesService.deleteLangue(id);
        return ResponseEntity.noContent().build();
    }
}