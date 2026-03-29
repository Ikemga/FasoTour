package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.paie.FactureDTO;
import egate.digital.fasotour.dto.paie.PaiementResponseDTO;
import egate.digital.fasotour.model.Facture;
import egate.digital.fasotour.model.Paiement;
import egate.digital.fasotour.model.Reservation;

public final class PaiementMapper {

    private PaiementMapper() {}

    public static PaiementResponseDTO toDTO(Paiement paiement) {
        if (paiement == null) return null;

        return new PaiementResponseDTO(
                paiement.getId(),
                paiement.getMontant(),
                paiement.getMontantPaye(),
                paiement.getDatePaiement(),
                paiement.getReferencePaie(),
                paiement.getStatut(),
                mapReservationId(paiement.getReservation()),
                mapFacture(paiement.getFacture())
        );
    }


    private static Long mapReservationId(Reservation reservation) {
        return reservation != null ? reservation.getId() : null;
    }

    private static FactureDTO mapFacture(Facture facture) {
        if (facture == null) return null;

        return new FactureDTO(
                facture.getId(),
                facture.getReference(),
                facture.getDateEmission(),
                facture.getMontantTotal(),
                facture.getMontantPaie(),
                facture.getMontantRestant()
        );
    }
}