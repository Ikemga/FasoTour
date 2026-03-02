package egate.digital.fasotour.dto.paie;

import java.util.Date;

public record FactureDTO(
        Long id,
        String reference,
        Date dateEmission,
        Double montatTotal,
        Double montantPaie,
        Double montantRestant
) {}