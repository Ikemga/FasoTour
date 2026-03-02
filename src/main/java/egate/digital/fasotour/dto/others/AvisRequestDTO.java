package egate.digital.fasotour.dto.others;

import java.time.LocalDate;

public record AvisRequestDTO(

        String avis,
        Long note,
        LocalDate date,

        Long circuitId,
        Long guideId,
        Long touristeId

) {}