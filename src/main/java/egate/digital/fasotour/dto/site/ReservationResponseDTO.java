package egate.digital.fasotour.dto.site;

import java.time.LocalDate;
import java.util.Date;

public record ReservationResponseDTO(

        Long id,
        int nombrePersonne,
        Double prixReservation,
        LocalDate dateResevation,
        String commentaire,
        String statut,
        Date dateLimitePaiement,

        Long paiementId,
        Long circuitId,
        String circuitNom,
        Long touristeId,
        String touristeNom

) {}