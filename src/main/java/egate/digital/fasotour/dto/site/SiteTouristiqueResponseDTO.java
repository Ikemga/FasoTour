package egate.digital.fasotour.dto.site;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record SiteTouristiqueResponseDTO(
        Long id,
        String nom,
        String description,
        String region,
        String image,
        Double noteMoyenne,
        String video,
        String fichier,
        LocalDateTime createedAt,
        String localisation,

        //Add attribut
        LocalTime heureOuverture,
        LocalTime heureFermeture,
        Double tarif,
        String statut,

        List<CategorieSimpledDTO> categories
) {

}
