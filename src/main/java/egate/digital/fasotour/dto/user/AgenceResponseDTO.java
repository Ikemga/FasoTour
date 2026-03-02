package egate.digital.fasotour.dto.user;

import java.util.Date;

public record AgenceResponseDTO(

        Long id,
        String nomComplet,
        String mail,
        String sitWeb,
        String pageFacebook,
        String numeroAgrement,
        Date dateCreation

) {}