package egate.digital.fasotour.dto.site;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import egate.digital.fasotour.dto.user.AgenceSimpleDTO;
import egate.digital.fasotour.dto.user.GuideSimpleDTO;

public record CircuitResponseDTO(

    Long id,
    String circuitName,
    LocalDate dateDebut,
    LocalDate dateFin,
    String description,
    Long duree,
    Double prixIndividuel,
    Integer nombreExact,
    Integer nombreRestant,
    LocalDateTime createdAt,
    String statut,
    boolean transport,

    //Add attribut
    String lieuRassemblement,
    LocalTime heureDepart,
    LocalDate dateLimiteReservation,
    String  image,

    List<GuideSimpleDTO> guide,
    AgenceSimpleDTO agence,
    List<SiteSimpleDTO> sites

) {}