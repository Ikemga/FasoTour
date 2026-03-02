package egate.digital.fasotour.dto.paie;

import java.util.Date;

public record PaiementResponseDTO(
        Long id,
        Double montant,
        Double montantPaye,
        Date datePaiement,
        String reference,
        String statut,
        Long reservationId,
        FactureDTO facture
) {}
