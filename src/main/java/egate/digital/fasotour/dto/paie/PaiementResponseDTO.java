package egate.digital.fasotour.dto.paie;

import java.time.LocalDate;

public record PaiementResponseDTO(
        Long id,
        Double montant,
        Double montantPaye,
        LocalDate datePaiement,
        String referencePaie,
        String statut,
        Long reservationId,
        FactureDTO facture
) {}
