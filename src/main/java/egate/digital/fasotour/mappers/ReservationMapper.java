package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.site.ReservationRequestDTO;
import egate.digital.fasotour.dto.site.ReservationResponseDTO;
import egate.digital.fasotour.model.*;

public class ReservationMapper {

    private ReservationMapper() {}

    /**
     * dto to ety
    */
    public static Reservation toEntity(
            ReservationRequestDTO dto,
            Paiement paiement,
            Circuit circuit,
            Touriste touriste
    ) {

        Reservation reservation = new Reservation();

        reservation.setNombrePersonne(dto.nombrePersonne());
        reservation.setPrixReservation(dto.prixReservation());
        reservation.setDateResevation(dto.dateResevation());
        reservation.setCommentaire(dto.commentaire());
        reservation.setStatut(dto.statut());
        reservation.setDateLimitePaiement(dto.dateLimitePaiement());

        reservation.setPaiement(paiement);
        reservation.setCircuit(circuit);
        reservation.setTouriste(touriste);

        return reservation;
    }


    public static ReservationResponseDTO toDTO(Reservation reservation) {

        Long paiementId = null;
        Long circuitId = null;
        String circuitNom = null;
        Long touristeId = null;
        String touristeNom = null;

        if (reservation.getPaiement() != null) {
            paiementId = reservation.getPaiement().getId();
        }

        if (reservation.getCircuit() != null) {
            circuitId = reservation.getCircuit().getId();
            circuitNom = reservation.getCircuit().getCircuitName();
        }

        if (reservation.getTouriste() != null) {
            touristeId = reservation.getTouriste().getId();
            touristeNom = reservation.getTouriste().getNomComplet();
        }

        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getNombrePersonne(),
                reservation.getPrixReservation(),
                reservation.getDateResevation(),
                reservation.getCommentaire(),
                reservation.getStatut(),
                reservation.getDateLimitePaiement(),
                paiementId,
                circuitId,
                circuitNom,
                touristeId,
                touristeNom
        );
    }
}