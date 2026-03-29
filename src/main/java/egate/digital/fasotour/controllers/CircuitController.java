package egate.digital.fasotour.controllers;

import egate.digital.fasotour.dto.site.CircuitRequestDTO;
import egate.digital.fasotour.dto.site.CircuitResponseDTO;
import egate.digital.fasotour.services.CircuitService;
import egate.digital.fasotour.config.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/circuits")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CircuitController {

    private final CircuitService     circuitService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<CircuitResponseDTO>> getAllCircuits() {
        return ResponseEntity.ok(circuitService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CircuitResponseDTO> getCircuitById(@PathVariable Long id) {
        return ResponseEntity.ok(circuitService.getById(id));
    }

    @GetMapping("/order/date/desc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByDateDesc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByCreatedAtDesc());
    }

    @GetMapping("/order/date/asc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByDateAsc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByCreatedAtAsc());
    }

    @GetMapping("/order/name/asc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByNameAsc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByNameAsc());
    }

    @GetMapping("/order/name/desc")
    public ResponseEntity<List<CircuitResponseDTO>> getOrderByNameDesc() {
        return ResponseEntity.ok(circuitService.getCircuitsOrderByNameDesc());
    }

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CircuitResponseDTO> createCircuit(
            @RequestParam("circuitName")                                   String circuitName,
            @RequestParam(value = "description",           required = false) String description,
            @RequestParam("dateDebut")                                     String dateDebut,
            @RequestParam("dateFin")                                       String dateFin,
            @RequestParam("dateLimiteReservation")                         String dateLimiteReservation,
            @RequestParam(value = "lieuRassemblement",     required = false) String lieuRassemblement,
            @RequestParam(value = "heureDepart",           required = false) String heureDepart,
            @RequestParam("prixIndividuel")                                String prixIndividuel,
            @RequestParam("nombreExact")                                   String nombreExact,
            @RequestParam("statut")                                        String statut,
            @RequestParam("transport")                                     String transport,
            @RequestParam("agenceId")                                      String agenceId,
            @RequestParam(value = "siteIds",               required = false) List<String> siteIds,
            @RequestParam(value = "guideIds",              required = false) List<String> guideIds,
            @RequestPart(value = "images",                 required = false) List<MultipartFile> images
    ) {
        String imageUrl = null;
        if (images != null && !images.isEmpty()) {
            imageUrl = fileStorageService.save(images.get(0));
        }

        CircuitRequestDTO dto = CircuitRequestDTO.fromParts(
                circuitName, description, dateDebut, dateFin, dateLimiteReservation,
                lieuRassemblement, heureDepart, prixIndividuel, nombreExact,
                statut, transport, agenceId, siteIds, guideIds, imageUrl
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(circuitService.createCircuit(dto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CircuitResponseDTO> updateCircuit(
            @PathVariable Long id,
            @RequestParam("circuitName")                                   String circuitName,
            @RequestParam(value = "description",           required = false) String description,
            @RequestParam("dateDebut")                                     String dateDebut,
            @RequestParam("dateFin")                                       String dateFin,
            @RequestParam("dateLimiteReservation")                         String dateLimiteReservation,
            @RequestParam(value = "lieuRassemblement",     required = false) String lieuRassemblement,
            @RequestParam(value = "heureDepart",           required = false) String heureDepart,
            @RequestParam("prixIndividuel")                                String prixIndividuel,
            @RequestParam("nombreExact")                                   String nombreExact,
            @RequestParam("statut")                                        String statut,
            @RequestParam("transport")                                     String transport,
            @RequestParam("agenceId")                                      String agenceId,
            @RequestParam(value = "siteIds",               required = false) List<String> siteIds,
            @RequestParam(value = "guideIds",              required = false) List<String> guideIds,
            @RequestPart(value = "images",                 required = false) List<MultipartFile> images
    ) {
        String imageUrl = null;
        if (images != null && !images.isEmpty()) {
            imageUrl = fileStorageService.save(images.get(0));
        }

        CircuitRequestDTO dto = CircuitRequestDTO.fromParts(
                circuitName, description, dateDebut, dateFin, dateLimiteReservation,
                lieuRassemblement, heureDepart, prixIndividuel, nombreExact,
                statut, transport, agenceId, siteIds, guideIds, imageUrl
        );

        return ResponseEntity.ok(circuitService.updatcircuite(id, dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<CircuitResponseDTO>> createBatch(@RequestBody List<CircuitRequestDTO> dtos) {
        List<CircuitResponseDTO> result = new ArrayList<>(dtos).stream()
                .map(circuitService::createCircuit)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
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