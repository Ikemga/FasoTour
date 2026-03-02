package egate.digital.fasotour.dto.user;

import java.util.Date;
import java.util.Set;

public record AgenceRequestDTO(

        String nomComplet,
        String adresse,
        String mail,
        String telephone,
        String motDePasse,
        String pays,
        String photo,
        String bio,

        String sitWeb,
        String pageFacebook,
        String numeroAgrement,
        Date dateCreation,
        String statut,

        Set<Long> langueIds,
        Set<Long> roleIds,
        Set<Long> guideIds

) {}