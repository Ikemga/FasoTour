package egate.digital.fasotour.dto.site;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record CircuitRequestDTO(

    String circuitName,
    Date dateDebut,
    Date dateFin,
    String description,
    String duree,
    Double prixIndividuel,
    int nombreMini,
    int nombreMax,
    int nombreExact,
    LocalDateTime createdAt,
    String statut,
    boolean transport,

    List<Long> guideIds,
    Long agenceId,
    List<Long> siteIds

) {}