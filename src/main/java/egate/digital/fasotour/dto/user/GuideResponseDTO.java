package egate.digital.fasotour.dto.user;

import java.time.LocalDateTime;
import java.util.Set;

public record GuideResponseDTO(

        Long id,
        String nomComplet,
        String mail,
        String telephone,
        String experience,
        String preferenceTouristique,
        Boolean actif,
        LocalDateTime createAt,
        Set<String> langues


) {}