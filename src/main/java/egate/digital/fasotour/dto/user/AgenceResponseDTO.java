package egate.digital.fasotour.dto.user;

import java.time.LocalDateTime;
import java.util.Date;

public record AgenceResponseDTO(

        Long id,
        String nomComplet,
        String mail,
        String telephone,
        Boolean actif,
        LocalDateTime createAt,
        String sitWeb,
        String pageFacebook,
        String numeroAgrement,
        Date dateCreation

) {}