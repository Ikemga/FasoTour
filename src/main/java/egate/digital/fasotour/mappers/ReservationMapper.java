package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.site.ReservationRequestDTO;
import egate.digital.fasotour.dto.site.ReservationResponseDTO;
import egate.digital.fasotour.model.*;

import java.time.LocalDate;

public class ReservationMapper {

    private ReservationMapper() {}

    /**
     * Convertit un ReservationRequestDTO en Reservation (entité)
     */
    public static Reservation toEntity(
            ReservationRequestDTO dto,
            Paiement paiement,
            Circuit circuit,
            Touriste touriste
    ) {
        Reservation reservation = new Reservation();

        reservation.setNombrePersonne(dto.nombrePersonne());

        // Calcul automatique des prix
        double prixCircuit = circuit.getPrixIndividuel() * dto.nombrePersonne();
        double fraisReservation = 200.0;
        double montantTotal = prixCircuit + fraisReservation;

        reservation.setPrixCircuit(prixCircuit);
        reservation.setFraisReservation(fraisReservation);
        reservation.setMontantTotal(montantTotal);

        reservation.setCommentaire(dto.commentaire());
        reservation.setStatut("EN_ATTENTE");
        reservation.setReference(null);
        reservation.setDateResevation(LocalDate.now());
        reservation.setDateLimitePaiement(dto.dateLimitePaiement());

        reservation.setPaiement(paiement);
        reservation.setCircuit(circuit);
        reservation.setTouriste(touriste);

        return reservation;
    }

    /**
     * Convertit une entité Reservation en DTO de réponse
     */
    public static ReservationResponseDTO toDTO(Reservation reservation) {

        Long paiementId = null;
        String referencePaie = null;
        if (reservation.getPaiement() != null) {
            paiementId = reservation.getPaiement().getId();
            referencePaie = reservation.getPaiement().getReferencePaie();
        }

        Long circuitId = null;
        String circuitNom = null;
        if (reservation.getCircuit() != null) {
            circuitId = reservation.getCircuit().getId();
            circuitNom = reservation.getCircuit().getCircuitName();
        }

        Long touristeId = null;
        String touristeNom = null;
        if (reservation.getTouriste() != null) {
            touristeId = reservation.getTouriste().getId();
            touristeNom = reservation.getTouriste().getNomComplet();
        }

        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getNombrePersonne(),
                reservation.getPrixCircuit(),
                reservation.getFraisReservation(),
                reservation.getMontantTotal(),
                reservation.getReference(),
                reservation.getCommentaire(),
                reservation.getStatut(),
                reservation.getDateResevation(),
                reservation.getDateLimitePaiement(),
                paiementId,
                referencePaie,
                circuitId,
                circuitNom,
                touristeId,
                touristeNom
        );
    }
}