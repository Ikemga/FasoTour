package egate.digital.fasotour.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import egate.digital.fasotour.mappers.ReservationMapper;
import egate.digital.fasotour.util.SequenceRefService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.site.ReservationRequestDTO;
import egate.digital.fasotour.dto.site.ReservationResponseDTO;
import egate.digital.fasotour.model.Circuit;
import egate.digital.fasotour.model.Paiement;
import egate.digital.fasotour.model.Reservation;
import egate.digital.fasotour.model.Touriste;
import egate.digital.fasotour.repository.CircuitRepository;
import egate.digital.fasotour.repository.PaiementRepository;
import egate.digital.fasotour.repository.ReservationRepository;
import egate.digital.fasotour.repository.TouristeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CircuitRepository circuitRepository;
    private final TouristeRepository touristeRepository;
    private final PaiementRepository paiementRepository;
    private final SequenceRefService sequenceRefService;

    public ReservationResponseDTO create(ReservationRequestDTO dto) {

        Circuit circuit = circuitRepository.findById(dto.circuitId())
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé"));

        Touriste touriste = touristeRepository.findById(dto.touristeId())
                .orElseThrow(() -> new RuntimeException("Touriste non trouvé"));

        Paiement paiement = null;
        if (dto.paiementId() != null) {
            paiement = paiementRepository.findById(dto.paiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        }

        // Vérification du nombre de personnes
        if (dto.nombrePersonne() <= 0 || dto.nombrePersonne() > circuit.getNombreRestant()) {
            throw new RuntimeException("Nombre de personnes invalide : doit être entre 1 et " + circuit.getNombreRestant());
        }

        // Création de la réservation
        Reservation reservation = ReservationMapper.toEntity(dto, paiement, circuit, touriste);

        // Génération de référence
        reservation.setReference(sequenceRefService.nextRef("RES"));

        // Déduction du nombre de places
        circuit.setNombreRestant(circuit.getNombreRestant() - dto.nombrePersonne());
        circuitRepository.save(circuit);

        return ReservationMapper.toDTO(reservationRepository.save(reservation));
    }

    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        Circuit circuit = reservation.getCircuit();
        Touriste touriste = reservation.getTouriste();
        Paiement paiement = reservation.getPaiement();

        if (dto.circuitId() != null && !dto.circuitId().equals(circuit.getId())) {
            circuit = circuitRepository.findById(dto.circuitId())
                    .orElseThrow(() -> new RuntimeException("Circuit non trouvé"));
        }

        if (dto.touristeId() != null && !dto.touristeId().equals(touriste.getId())) {
            touriste = touristeRepository.findById(dto.touristeId())
                    .orElseThrow(() -> new RuntimeException("Touriste non trouvé"));
        }

        if (dto.paiementId() != null) {
            paiement = paiementRepository.findById(dto.paiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        }

        // Mise à jour via mapper (recalcul des prix)
        Reservation updated = ReservationMapper.toEntity(dto, paiement, circuit, touriste);
        reservation.setNombrePersonne(updated.getNombrePersonne());
        reservation.setPrixCircuit(updated.getPrixCircuit());
        reservation.setFraisReservation(updated.getFraisReservation());
        reservation.setMontantTotal(updated.getMontantTotal());
        reservation.setCommentaire(updated.getCommentaire());
        reservation.setStatut(updated.getStatut());
        reservation.setDateLimitePaiement(updated.getDateLimitePaiement());
        reservation.setCircuit(circuit);
        reservation.setTouriste(touriste);
        reservation.setPaiement(paiement);

        return ReservationMapper.toDTO(reservationRepository.save(reservation));
    }

    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        Circuit circuit = reservation.getCircuit();
        if (circuit != null) {
            circuit.setNombreRestant(circuit.getNombreRestant() + reservation.getNombrePersonne());
            circuitRepository.save(circuit);
        }

        reservationRepository.delete(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDTO> getAllPaginated(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(ReservationMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO getById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getByStatut(String statut) {
        return reservationRepository.findByStatut(statut)
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getByDate(LocalDate date) {
        return reservationRepository.findByDateResevation(date)
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }
}