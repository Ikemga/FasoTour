package egate.digital.fasotour.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.StatiqueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.site.*;
import egate.digital.fasotour.model.*;
import egate.digital.fasotour.repository.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CircuitRepository circuitRepository;
    private final TouristeRepository touristeRepository;
    private final PaiementRepository paiementRepository;

    public ReservationResponseDTO create(ReservationRequestDTO dto) {

        Circuit circuit = circuitRepository.findById(dto.circuitId())
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé"));

        Touriste touriste = touristeRepository.findById(dto.touristeId())
                .orElseThrow(() -> new RuntimeException("Touriste non trouvé"));

        Reservation reservation = new Reservation();
        mapRequestToEntity(dto, reservation);

        reservation.setCircuit(circuit);
        reservation.setTouriste(touriste);

        if (dto.paiementId() != null) {
            Paiement paiement = paiementRepository.findById(dto.paiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
            reservation.setPaiement(paiement);
        }

        return mapToDTO(reservationRepository.save(reservation));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDTO> getAllPaginated(Pageable pageable) {

        return reservationRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO getById(Long id) {
        return reservationRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
    }

    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        mapRequestToEntity(dto, reservation);

        if (dto.circuitId() != null) {
            Circuit circuit = circuitRepository.findById(dto.circuitId())
                    .orElseThrow(() -> new RuntimeException("Circuit non trouvé"));
            reservation.setCircuit(circuit);
        }

        if (dto.touristeId() != null) {
            Touriste touriste = touristeRepository.findById(dto.touristeId())
                    .orElseThrow(() -> new RuntimeException("Touriste non trouvé"));
            reservation.setTouriste(touriste);
        }

        if (dto.paiementId() != null) {
            Paiement paiement = paiementRepository.findById(dto.paiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
            reservation.setPaiement(paiement);
        }

        return mapToDTO(reservationRepository.save(reservation));
    }

    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Réservation non trouvée");
        }
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getByStatut(String statut) {
        return reservationRepository.findByStatut(statut)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getByDate(LocalDate date) {
        return reservationRepository.findByDateResevation(date)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private void mapRequestToEntity(ReservationRequestDTO dto, Reservation r) {
        r.setNombrePersonne(dto.nombrePersonne());
        r.setPrixReservation(dto.prixReservation());
        r.setDateResevation(dto.dateResevation());
        r.setCommentaire(dto.commentaire());
        r.setStatut(dto.statut());
        r.setDateLimitePaiement(dto.dateLimitePaiement());
    }

    private ReservationResponseDTO mapToDTO(Reservation r) {
        return new ReservationResponseDTO(
                r.getId(),
                r.getNombrePersonne(),
                r.getPrixReservation(),
                r.getDateResevation(),
                r.getCommentaire(),
                r.getStatut(),
                r.getDateLimitePaiement(),
                r.getPaiement() != null ? r.getPaiement().getId() : null,
                r.getCircuit() != null ? r.getCircuit().getId() : null,
                r.getCircuit() != null ? r.getCircuit().getCircuitName() : null,
                r.getTouriste() != null ? r.getTouriste().getId() : null,
                r.getTouriste() != null ? r.getTouriste().getNomComplet() : null
        );
    }

    // Count By moth

/*
    @Transactional(readOnly = true)
    public List<StatiqueDTO> getReservationStatsByCircuit() {
        return reservationRepository.countReservationsByCircuit();
    }
    */
}