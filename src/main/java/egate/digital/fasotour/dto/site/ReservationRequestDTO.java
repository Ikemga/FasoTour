package egate.digital.fasotour.dto.site;

import java.time.LocalDate;
import java.util.Date;

public record ReservationRequestDTO(

        int nombrePersonne,
        String commentaire,
        LocalDate dateLimitePaiement,

        Long paiementId,
        Long circuitId,
        Long touristeId

) {}