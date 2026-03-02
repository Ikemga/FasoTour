package egate.digital.fasotour.dto.user;

import java.time.LocalDateTime;
import java.util.Set;

public record TouristeResponseDTO(

        Long id,
        String nomComplet,
        String mail,
        LocalDateTime createAt,
        String pays,
        Set<String> langues,
        Set<String> roles,

        String preferenceTouristique

) {}