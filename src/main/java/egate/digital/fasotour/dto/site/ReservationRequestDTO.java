package egate.digital.fasotour.dto.site;

import java.time.LocalDate;
import java.util.Date;

public record ReservationRequestDTO(

        int nombrePersonne,
        Double prixReservation,
        LocalDate dateResevation,
        String commentaire,
        String statut,
        Date dateLimitePaiement,

        Long paiementId,
        Long circuitId,
        Long touristeId

) {}