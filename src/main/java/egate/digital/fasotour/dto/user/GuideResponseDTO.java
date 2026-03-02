package egate.digital.fasotour.dto.user;

import java.util.Set;

public record GuideResponseDTO(

        Long id,
        String nomComplet,
        String mail,
        String experience,
        String preferenceTouristique,
        Set<String> langues


) {}