package egate.digital.fasotour.dto.site;

import java.time.LocalDateTime;
import java.util.List;

public record SiteTouristiqueResponseDTO(
    Long id,
    String nom,
    String description,
    String region,
    String image,
    String noteMoyenne,
    String video,
    String fichier,
    LocalDateTime createedAt,
    String localisation,

    List<CategorieResponseDTO> categories
) {

}
