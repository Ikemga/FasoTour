package egate.digital.fasotour.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egate.digital.fasotour.dto.paie.*;
import egate.digital.fasotour.mappers.PaiementMapper;
import egate.digital.fasotour.model.*;
import egate.digital.fasotour.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final ReservationRepository reservationRepository;

    public PaiementResponseDTO create(PaiementRequestDTO dto) {
        Reservation reservation = reservationRepository.findById(dto.reservationId())
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée : " + dto.reservationId()));

        if (reservation.getPaiement() != null)
            throw new RuntimeException("Cette réservation possède déjà un paiement.");

        Paiement paiement = new Paiement();
        paiement.setMontant(dto.montant());
        paiement.setMontantPaye(dto.montantPaye());
        paiement.setDatePaiement(dto.datePaiement());
        paiement.setReference(dto.reference());
        paiement.setStatut(dto.statut());
        paiement.setReservation(reservation);
        reservation.setPaiement(paiement);

        // Créer facture automatique
        Facture facture = new Facture();
        facture.setMontatTotal(dto.montant());
        facture.setMontantPaie(dto.montantPaye() != null ? dto.montantPaye() : 0.0);
        facture.setMontantRestant(facture.getMontatTotal() - facture.getMontantPaie());
        facture.setDateEmission(new Date());
        facture.setReference("FACT-" + System.currentTimeMillis());
        facture.setPaiement(paiement);
        paiement.setFacture(facture);

        return PaiementMapper.toDTO(paiementRepository.save(paiement));
    }

    public PaiementResponseDTO update(Long id, PaiementRequestDTO dto) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé : " + id));

        paiement.setMontant(dto.montant());
        paiement.setMontantPaye(dto.montantPaye());
        paiement.setDatePaiement(dto.datePaiement());
        paiement.setReference(dto.reference());
        paiement.setStatut(dto.statut());

        Facture facture = paiement.getFacture();
        if (facture != null) {
            facture.setMontatTotal(dto.montant());
            facture.setMontantPaie(dto.montantPaye() != null ? dto.montantPaye() : 0.0);
            facture.setMontantRestant(facture.getMontatTotal() - facture.getMontantPaie());
        }

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

        if (paiement.getReservation() != null) {
            paiement.getReservation().setPaiement(null);
        }

        paiementRepository.delete(paiement);
    }
}