package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.CategorieRequestDTO;
import egate.digital.fasotour.dto.site.CategorieResponseDTO;
import egate.digital.fasotour.model.SiteTouristique;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.dto.site.SiteTouristiqueRequestDTO;
import egate.digital.fasotour.dto.site.SiteTouristiqueResponseDTO;
import egate.digital.fasotour.services.SiteTouristiqueService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/sites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SiteTouristiqueController {

    private final SiteTouristiqueService siteTouristiqueService;

    // GET /api/v1/sites?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<SiteTouristiqueResponseDTO>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(siteTouristiqueService.findAll(pageable));
    }


    @GetMapping("/all")
    public List<SiteTouristiqueResponseDTO> getSites() {
        return siteTouristiqueService.getAllSitesNewestFirst();
    }

    //Search
    @GetMapping("/search")
    public ResponseEntity<List<SiteTouristiqueResponseDTO>> search(
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(siteTouristiqueService.search(q));
    }

    // GET /api/v1/sites/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SiteTouristiqueResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(siteTouristiqueService.findById(id));
    }

    // GET /api/v1/sites/region/{region}
    @GetMapping("/region/{region}")
    public ResponseEntity<List<SiteTouristiqueResponseDTO>> getByRegion(
            @PathVariable String region) {
        return ResponseEntity.ok(siteTouristiqueService.findByRegion(region));
    }

    @GetMapping("/alphabetical")
    public ResponseEntity<List<SiteTouristiqueResponseDTO>> getAllAlphabetical() {
        return ResponseEntity.ok(siteTouristiqueService.getAllAlphabetical());
    }
    
    // Site mager --------------------------------------------------------------------
    // POST /api/v1/sites
    @PostMapping
    public ResponseEntity<SiteTouristiqueResponseDTO> create(
            @RequestBody SiteTouristiqueRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(siteTouristiqueService.create(dto));
    }

    // POST /api/v1/sites/batch Many
    @PostMapping("batch")
    public ResponseEntity<List<SiteTouristiqueResponseDTO>> createBatch(
            @RequestBody List<SiteTouristiqueRequestDTO> dtos) {
        List<SiteTouristiqueResponseDTO> result = dtos.stream()
                .map(siteTouristiqueService::create)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Site mager --------------------------------------------------------------------



    // /api/v1/sites/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SiteTouristiqueResponseDTO> update(
            @PathVariable Long id,
            @RequestBody SiteTouristiqueRequestDTO dto) {
        return ResponseEntity.ok(siteTouristiqueService.update(id, dto));
    }

    // DELETE /api/v1/sites/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        siteTouristiqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}