package egate.digital.fasotour.dto.user;

import java.util.Set;

public record GuideRequestDTO(

        String nomComplet,
        String adresse,
        String mail,
        String telephone,
        String motDePasse,
        String pays,
        String photo,
        String bio,

        String experience,
        String preferenceTouristique,

        Set<Long> langueIds,
        Set<Long> roleIds,
        Set<Long> agenceIds

) {}