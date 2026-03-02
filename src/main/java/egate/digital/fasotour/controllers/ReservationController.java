package egate.digital.fasotour.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.StatiqueDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import egate.digital.fasotour.dto.site.*;
import egate.digital.fasotour.services.ReservationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // CREATE
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(
            @RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.ok(reservationService.create(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ReservationResponseDTO>> createBatch(
            @RequestBody List<ReservationRequestDTO> dtos) {
        List<ReservationResponseDTO> result = dtos.stream()
                .map(reservationService::create)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ReservationResponseDTO>> getPaginated(Pageable pageable) {
        return ResponseEntity.ok(reservationService.getAllPaginated(pageable));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET BY STATUT
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ReservationResponseDTO>> getByStatut(
            @PathVariable String statut) {
        return ResponseEntity.ok(reservationService.getByStatut(statut));
    }

    // GET BY DATE
    @GetMapping("/date")
    public ResponseEntity<List<ReservationResponseDTO>> getByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return ResponseEntity.ok(reservationService.getByDate(date));
    }

    /*
    @GetMapping("/stats/circuits")
    public ResponseEntity<List<StatiqueDTO>> getStatsByCircuit() {
        return ResponseEntity.ok(reservationService.getReservationStatsByCircuit());
    }
    */
}