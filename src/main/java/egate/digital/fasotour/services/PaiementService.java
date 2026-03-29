package egate.digital.fasotour.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import egate.digital.fasotour.dto.paie.*;
import egate.digital.fasotour.mappers.PaiementMapper;
import egate.digital.fasotour.model.*;
import egate.digital.fasotour.repository.*;
import egate.digital.fasotour.util.SequenceRefService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final ReservationRepository reservationRepository;
    private final SequenceRefService sequenceRefService;


    public PaiementResponseDTO create(PaiementRequestDTO dto) {

        Reservation reservation = reservationRepository.findById(dto.reservationId())
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée : " + dto.reservationId()));

        if (reservation.getPaiement() != null) {
            throw new RuntimeException("Cette réservation possède déjà un paiement.");
        }

        Double montantTotal = reservation.getMontantTotal();
        Double montantPaye = dto.montantPaye() != null ? dto.montantPaye() : 0.0;

        if (montantPaye <= 0) {
            throw new RuntimeException("Le montant payé doit être supérieur à 0.");
        }

        if (montantPaye > montantTotal) {
            throw new RuntimeException("Montant payé supérieur au montant total.");
        }


        Paiement paiement = new Paiement();
        paiement.setReferencePaie(sequenceRefService.nextRef("PAIE"));
        paiement.setMontant(montantTotal);
        paiement.setMontantPaye(montantPaye);
        paiement.setDatePaiement(LocalDate.now());
        paiement.setStatut(calculateStatut(montantTotal, montantPaye));
        paiement.setReservation(reservation);

        Facture facture = new Facture();
        facture.setReference(sequenceRefService.nextRef("FACT"));
        facture.setDateEmission(LocalDate.now());
        facture.setMontantTotal(montantTotal);
        facture.setMontantPaie(montantPaye);
        facture.setMontantRestant(montantTotal - montantPaye);
        facture.setPaiement(paiement);

        paiement.setFacture(facture);

        reservation.setPaiement(paiement);
        updateReservationStatut(reservation, montantTotal, montantPaye);

        return PaiementMapper.toDTO(paiementRepository.save(paiement));
    }

    public PaiementResponseDTO update(Long id, PaiementRequestDTO dto) {

        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé : " + id));

        Double montantTotal = paiement.getMontant();
        //Double ancienMontant = paiement.getMontantPaye();
        Double nouveauMontant = dto.montantPaye() != null ? dto.montantPaye() : 0.0;

        if (nouveauMontant <= 0) {
            throw new RuntimeException("Le montant payé doit être supérieur à 0.");
        }

        if (nouveauMontant > montantTotal) {
            throw new RuntimeException("Montant payé supérieur au montant total.");
        }

        paiement.setMontantPaye(nouveauMontant);
        paiement.setStatut(calculateStatut(montantTotal, nouveauMontant));

        Facture facture = paiement.getFacture();
        if (facture != null) {
            facture.setMontantPaie(nouveauMontant);
            facture.setMontantRestant(montantTotal - nouveauMontant);
        }

        Reservation reservation = paiement.getReservation();
        updateReservationStatut(reservation, montantTotal, nouveauMontant);

        return PaiementMapper.toDTO(paiementRepository.save(paiement));
    }

    public PaiementResponseDTO getById(Long id) {
        return PaiementMapper.toDTO(
                paiementRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Paiement non trouvé : " + id))
        );
    }

    public List<PaiementResponseDTO> getAll() {
        return paiementRepository.findAll()
                .stream()
                .map(PaiementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {

        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé : " + id));

        Reservation reservation = paiement.getReservation();

        if (reservation != null) {
            reservation.setPaiement(null);
            reservation.setStatut("EN_ATTENTE"); // reset
        }

        paiementRepository.delete(paiement);
    }


    private String calculateStatut(Double total, Double paye) {
        if (paye == 0) return "EN_ATTENTE";
        if (paye < total) return "PARTIEL";
        return "PAYE";
    }

    private void updateReservationStatut(Reservation reservation, Double total, Double paye) {
        if (paye == 0) {
            reservation.setStatut("EN_ATTENTE");
        } else if (paye < total) {
            reservation.setStatut("PAYE_PARTIEL");
        } else {
            reservation.setStatut("CONFIRMEE");
        }
    }
}