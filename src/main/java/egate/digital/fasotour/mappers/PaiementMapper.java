package egate.digital.fasotour.mappers;

import egate.digital.fasotour.dto.paie.FactureDTO;
import egate.digital.fasotour.dto.paie.PaiementResponseDTO;
import egate.digital.fasotour.model.*;
import egate.digital.fasotour.dto.paie.*;

public class PaiementMapper {

    private PaiementMapper() {}

    public static PaiementResponseDTO toDTO(Paiement paiement) {
        if (paiement == null) return null;

        Facture facture = paiement.getFacture();
        FactureDTO factureDTO = null;
        if (facture != null) {
            factureDTO = new FactureDTO(
                    facture.getId(),
                    facture.getReference(),
                    facture.getDateEmission(),
                    facture.getMontatTotal(),
                    facture.getMontantPaie(),
                    facture.getMontantRestant()
            );
        }

        Long reservationId = paiement.getReservation() != null ? paiement.getReservation().getId() : null;

        return new PaiementResponseDTO(
                paiement.getId(),
                paiement.getMontant(),
                paiement.getMontantPaye(),
                paiement.getDatePaiement(),
                paiement.getReference(),
                paiement.getStatut(),
                reservationId,
                factureDTO
        );
    }
}