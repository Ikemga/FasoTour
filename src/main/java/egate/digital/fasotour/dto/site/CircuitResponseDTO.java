package egate.digital.fasotour.dto.site;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import egate.digital.fasotour.dto.user.AgenceSimpleDTO;
import egate.digital.fasotour.dto.user.GuideSimpleDTO;

public record CircuitResponseDTO(

    Long id,
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

    List<GuideSimpleDTO> guide,
    AgenceSimpleDTO agence,
    List<SiteSimpleDTO> sites

) {}