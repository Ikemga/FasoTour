package egate.digital.fasotour.dto.others;

import java.time.LocalDate;

public record AvisResponseDTO(

        Long id,
        String avis,
        Long note,
        LocalDate date,

        Long circuitId,
        String circuitNom,

        Long guideId,
        String guideNom,

        Long touristeId,
        String touristeNom

) {}