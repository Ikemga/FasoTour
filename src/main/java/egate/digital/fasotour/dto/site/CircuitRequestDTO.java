package egate.digital.fasotour.dto.site;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record CircuitRequestDTO(

    String circuitName,
    Date dateDebut,
    Date dateFin,
    String description,
    Duration duree,
    Double prixIndividuel,
    int nombreMini,
    int nombreMax,
    int nombreExact,
    String statut,
    boolean transport,

    // Add attribut
    String lieuRassemblement,
    LocalTime heureDepart,
    LocalDateTime dateLimiteReservation,

    List<Long> guideIds,
    Long agenceId,
    List<Long> siteIds

) {}