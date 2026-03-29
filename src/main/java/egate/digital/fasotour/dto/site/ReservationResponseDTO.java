package egate.digital.fasotour.dto.site;

import java.time.LocalDate;

public record ReservationResponseDTO(

        Long id,
        int nombrePersonne,

        Double prixCircuit,
        Double fraisReservation,
        Double montantTotal,

        String reference,
        String commentaire,
        String statut,

        LocalDate dateResevation,
        LocalDate dateLimitePaiement,

        Long paiementId,
        String referencePaie,
        Long circuitId,
        String circuitName,
        Long touristeId,
        String nomComplet

) {}

