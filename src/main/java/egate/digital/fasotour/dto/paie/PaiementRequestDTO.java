package egate.digital.fasotour.dto.paie;


public record PaiementRequestDTO(
        Double montantPaye,
        //
        Long reservationId,
        Long factureId
) {}