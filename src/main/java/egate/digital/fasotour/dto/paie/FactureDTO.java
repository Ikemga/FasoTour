package egate.digital.fasotour.dto.paie;

import java.time.LocalDate;

public record FactureDTO(
        Long id,
        String reference,
        LocalDate dateEmission,
        Double montantTotal,
        Double montantPaie,
        Double montantRestant
) {}